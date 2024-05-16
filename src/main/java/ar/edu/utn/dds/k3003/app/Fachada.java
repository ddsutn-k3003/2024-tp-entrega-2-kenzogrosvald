package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaHeladeras;
import ar.edu.utn.dds.k3003.facades.FachadaLogistica;
import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.*;
import ar.edu.utn.dds.k3003.facades.exceptions.TrasladoNoAsignableException;
import ar.edu.utn.dds.k3003.model.Ruta;
import ar.edu.utn.dds.k3003.model.Traslado;
import ar.edu.utn.dds.k3003.repositories.RutaMapper;
import ar.edu.utn.dds.k3003.repositories.RutaRepository;
import ar.edu.utn.dds.k3003.repositories.TrasladoMapper;
import ar.edu.utn.dds.k3003.repositories.TrasladoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class Fachada implements FachadaLogistica {

    private final RutaRepository rutaRepository = new RutaRepository();
    private final RutaMapper rutaMapper = new RutaMapper();
    private final TrasladoRepository trasladoRepository = new TrasladoRepository();
    private final TrasladoMapper trasladoMapper = new TrasladoMapper();
    private FachadaViandas fachadaViandas;
    private FachadaHeladeras fachadaHeladeras;

    /*
    * "Un colaborador de transporte establece que puede llevar
    * una vianda entre 2 heladeras"
    * */

    @Override
    public RutaDTO agregar(RutaDTO rutaDTO) {
        Ruta ruta_sin_id = new Ruta(rutaDTO.getColaboradorId(), rutaDTO.getHeladeraIdOrigen(), rutaDTO.getHeladeraIdDestino());
        Ruta ruta_con_id = this.rutaRepository.save(ruta_sin_id);
        return rutaMapper.map(ruta_con_id);
    }

    public Ruta buscarRutaXOrigenYDestino(Integer Origen, Integer Destino) throws NoSuchElementException {
        List<Ruta> rutas = this.rutaRepository.findByHeladeras(Origen, Destino);
        if (rutas.isEmpty()) {
            throw new NoSuchElementException();
        }
        return rutas.get(0);
    }

    /*
    * Dado un id de traslado, se busca el mismo en el repositorio de traslados
    * y se devuelve un DTO con la información del traslado, o se lanza una excepción
    * si no se encuentra el traslado del tipo NoSuchElementException
    * */

    @Override
    public TrasladoDTO buscarXId(Long idTraslado) throws NoSuchElementException {
        Traslado traslado = this.trasladoRepository.findById(idTraslado);
        TrasladoDTO trasladoDTO= new TrasladoDTO(traslado.getQrVianda(), traslado.getEstado(), traslado.getFechaTraslado(), traslado.getRuta().getHeladeraIdOrigen(), traslado.getRuta().getHeladeraIdDestino());
        trasladoDTO.setColaboradorId(traslado.getRuta().getColaboradorId());
        trasladoDTO.setId(traslado.getId());
        return trasladoDTO;
    }

    /*
    * "Cuando se determina que hay que llevar una vianda de una
    * heladera a otra, lo siguiente que hay que hacer es asignar
    * un transportista que se haya comprometido a cumplir esa ruta"
    * */

    @Override
    public TrasladoDTO asignarTraslado(TrasladoDTO trasladoDTO) throws TrasladoNoAsignableException {
        Ruta ruta;

        if(trasladoDTO.getQrVianda() == null) {
            throw new NoSuchElementException("El traslado no es asignable. No se encontró la vianda.");
        }

        // Si no se encuentra la ruta, se lanza una excepción
        try {
            // Intento buscar la ruta
            ruta = buscarRutaXOrigenYDestino(trasladoDTO.getHeladeraOrigen(), trasladoDTO.getHeladeraDestino());
        } catch (NoSuchElementException e) {
            throw new TrasladoNoAsignableException("El traslado no es asignable. No se encontró la ruta.");
        }

        // Esto tira NoSuchElementException si no encuentra la vianda
        // Lo comento porque no funciona el ViandasProxy
        // ViandaDTO vianda = this.fachadaViandas.buscarXQR(trasladoDTO.getQrVianda());

        // Si tanto la ruta como la vianda existen, procedo a crear y guardar el traslado
        Traslado traslado = this.trasladoRepository.save(
                new Traslado(
                        trasladoDTO.getQrVianda(),
                        ruta,
                        EstadoTrasladoEnum.ASIGNADO,
                        trasladoDTO.getFechaTraslado()
                )
        );

        // Crea un DTO con la información del traslado
        TrasladoDTO traslado_dto = new TrasladoDTO(
                traslado.getQrVianda(),
                traslado.getEstado(),
                traslado.getFechaTraslado(),
                traslado.getRuta().getHeladeraIdOrigen(),
                traslado.getRuta().getHeladeraIdDestino()
        );

        // Asigno el id del colaborador que está en la ruta al trasladoDTO
        traslado_dto.setColaboradorId(ruta.getColaboradorId());

        // Asigno el id del traslado generado al DTO
        traslado_dto.setId(traslado.getId());

        // Retorna un DTO con la información del traslado completa
        return traslado_dto;
    }

    /*
     * "Un colaborador de transporte puede ver los traslados que
     * ha realizado en un mes y año determinado"
     * */

    @Override
    public List<TrasladoDTO> trasladosDeColaborador(Long idColaborador, Integer mes, Integer anio) {
        List<Traslado> traslados = this.trasladoRepository.findByCollaboratorId(idColaborador, mes, anio);
        return traslados.stream()
                .map(this.trasladoMapper::map)
                .collect(Collectors.toList());
    }

    /*
    * "Una vez que un trasportista tiene una vianda asignada,
    * comienza el transporte de la vianda una vez que el mismo
    * la retira de la heladera"
    *
    * "Vianda: Depositada -> EnTraslado
    *  Transporte: Asignado -> En Progreso"
    * */

    @Override
    public void trasladoRetirado(Long idTraslado) {
        Traslado traslado = this.trasladoRepository.findById(idTraslado);
        ViandaDTO vianda = this.fachadaViandas.buscarXQR(traslado.getQrVianda());

        RetiroDTO retiro = new RetiroDTO(
                vianda.getCodigoQR(),
                "123456789",
                LocalDateTime.now(),
                traslado.getRuta().getHeladeraIdOrigen()
        );

        fachadaHeladeras.retirar(retiro);

        // Modifico los estados de la vianda y el traslado. Desprecio los retornos
        fachadaViandas.modificarEstado(vianda.getCodigoQR(), EstadoViandaEnum.EN_TRASLADO);
        this.trasladoRepository.modificarEstado(traslado.getId(), EstadoTrasladoEnum.EN_VIAJE);

    }

    /*
    * "Cuando el transportista deja la vianda en la heladera
    * de destino, se establece que el traslado esta completo"
    * "Vianda: EnTraslado -> Depositado
    *  Transporte: En Progreso -> Terminado"
    * */

    @Override
    public void trasladoDepositado(Long idTraslado) {
        Traslado traslado = this.trasladoRepository.findById(idTraslado);
        ViandaDTO vianda = this.fachadaViandas.buscarXQR(traslado.getQrVianda());

        fachadaViandas.modificarHeladera(vianda.getCodigoQR(), traslado.getRuta().getHeladeraIdDestino());

        // Modifico los estados de la vianda y el traslado. Desprecio los retornos
        fachadaViandas.modificarEstado(vianda.getCodigoQR(), EstadoViandaEnum.DEPOSITADA);
        this.trasladoRepository.modificarEstado(traslado.getId(), EstadoTrasladoEnum.ENTREGADO);
    }

    @Override
    public void setHeladerasProxy(FachadaHeladeras fachadaHeladeras) {
        this.fachadaHeladeras = fachadaHeladeras;
    }

    @Override
    public void setViandasProxy(FachadaViandas fachadaViandas) {
        this.fachadaViandas = fachadaViandas;
    }
}
