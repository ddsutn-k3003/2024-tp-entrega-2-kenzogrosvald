package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoTrasladoEnum;
import ar.edu.utn.dds.k3003.model.Traslado;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class TrasladoRepository {

    private static AtomicLong seqId = new AtomicLong();
    private Collection<Traslado> traslados;

    public TrasladoRepository(){
        this.traslados = new ArrayList<>();
    }

    public Traslado save(Traslado traslado) {
        if (Objects.isNull(traslado.getId())) {
            traslado.setId(seqId.getAndIncrement());
            this.traslados.add(traslado);
        }
        return traslado;
    }

    public Traslado findById(Long id) {
        Optional<Traslado> first = this.traslados.stream().filter(x -> x.getId().equals(id)).findFirst();
        return first.orElseThrow(() -> new NoSuchElementException(
                String.format("No hay un traslado de id: %s", id)
        ));
    }

    public List<Traslado> findByCollaboratorId(Long colaboradorId, Integer mes, Integer anio) {
        List<Traslado> trasladosDelColaborador = this.traslados.stream()
                .filter(x -> x.getCollaboratorId().equals(colaboradorId))
                .filter(x -> x.getFechaTraslado().getMonthValue() == mes)
                .filter(x -> x.getFechaTraslado().getYear() == anio)
                .collect(Collectors.toList());

        /* Lo dejo comentado porque el enunciado no pide que se lance una excepción si no hay traslados, pero estaria bueno, sino devuelvo una lista vacia

        if (trasladosDelColaborador.isEmpty()) {
            throw new NoSuchElementException(String.format("No hay traslados para ese colaborador: %s en el mes: %s y año: %s", colaboradorId, mes, anio));
        }
        */
        return trasladosDelColaborador;
    }

    public Traslado modificarEstado(Long idTraslado, EstadoTrasladoEnum estado) {
        Traslado traslado = this.findById(idTraslado);
        traslado.setEstado(estado);
        return traslado;
    }

}