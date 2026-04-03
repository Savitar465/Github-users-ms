package com.inspire.msusuarios.service.implementacion;

import com.inspire.mscommon.abstracts.EstadoAbstract;
import com.inspire.mscommon.criteria.SearchSpecification;
import com.inspire.mscommon.criteria.modelCriteria.SearchRequest;
import com.inspire.mscommon.dto.response.EliminarResponse;
import com.inspire.mscommon.exceptionhandler.exceptions.EntityConflictException;
import com.inspire.mscommon.exceptionhandler.exceptions.EntityNotFoundException;
import com.inspire.mscommon.model.Transaccion;
import com.inspire.msusuarios.dao.usuarios.ClienteDao;
import com.inspire.msusuarios.dto.request.ClienteRequest;
import com.inspire.msusuarios.model.usuarios.Cliente;
import com.inspire.msusuarios.service.contratos.ClienteService;
import com.inspire.msusuarios.service.contratos.SistemaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ClienteServiceImpl implements ClienteService {
    private final ClienteDao clienteDao;
    private final SistemaService sistemaService;

    public ClienteServiceImpl(ClienteDao clienteDao, SistemaService sistemaService) {
        this.clienteDao = clienteDao;
        this.sistemaService = sistemaService;
    }

    @Override
    public Page<Cliente> listarClientes(int pagina, int cantidad) {
        Pageable pageable = PageRequest.of(pagina, cantidad);
        return clienteDao.findAllByEstado(EstadoAbstract.ACTIVO,pageable);
    }

    @Override
    public Cliente clienteUnico(String clienteId) {
        return clienteDao.findById(clienteId).orElseThrow(() -> {
            log.error("No existe el cliente");
            return new EntityNotFoundException("No existe ningún cliente");
        });
    }

    @Override
    public Cliente crearCliente(ClienteRequest clienteRequest, Transaccion transaction) {
        if (!clienteDao.existsByClienteName(clienteRequest.getClienteName())) {
            Cliente cliente = Cliente.builder()
                    .clienteName(clienteRequest.getClienteName())
                    .sistemasSistemaId(sistemaService.obtenerSistema(clienteRequest.getSistemaId()))
                    .descripcion(clienteRequest.getDescripcion())
                    .tipo(clienteRequest.getTipo())
                    .config(clienteRequest.getConfig())
                    .estado(EstadoAbstract.ACTIVO)
                    .usuCre(transaction.getTrUsuarioId())
                    .fecCre(transaction.getTrFecha())
                    .usuMod(transaction.getTrUsuarioId())
                    .fecMod(transaction.getTrFecha())
                    .build();
            return clienteDao.save(cliente);
        } else {
            log.error("Cliente con ese nombre {} ya existe", clienteRequest.getClienteName());
            throw new EntityConflictException("El Cliente con el nombre " + clienteRequest.getClienteName() + " ya existe");
        }
    }

    @Override
    public Cliente editarCliente(String clienteId, ClienteRequest clienteRequest, Transaccion transaction) {
        Cliente cliente = clienteDao.findById(clienteId)
                .orElseThrow(() -> {
                    log.error("No se encontró un cliente con el identificador '{}'.", clienteId);
                    return new EntityNotFoundException("No existe un cliente registrado con el ID '" + clienteId + "'.");
                });
            cliente.setClienteName(clienteRequest.getClienteName());
            cliente.setDescripcion(clienteRequest.getDescripcion());
            cliente.setTipo(clienteRequest.getTipo());
            cliente.setConfig(clienteRequest.getConfig());
            cliente.setUsuMod(transaction.getTrUsuarioId());
            cliente.setFecMod(transaction.getTrFecha());
            return clienteDao.save(cliente);
    }

    @Override
    public EliminarResponse eliminarCliente(String clienteId) {
        Cliente cliente = clienteDao.findById(clienteId).orElseThrow(() -> {
            log.error("no existe el cliente con id: {}", clienteId);
            return new EntityNotFoundException("No existe el cliente con id: " + clienteId);
        });
        clienteDao.delete(cliente);
        log.info("Se eliminó físicamente el cliente con id: {}", clienteId);
        return new EliminarResponse(clienteId, "Se eliminó el cliente permanentemente");
    }

    @Override
    public Page<Cliente> listarSearchClientes(SearchRequest searchRequest) {
        SearchSpecification<Cliente> specificationCliente = new SearchSpecification<>(searchRequest);
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize());
        log.info("Cliente: {}", clienteDao.findAll(specificationCliente, pageable));
        return clienteDao.findAll(specificationCliente, pageable);
    }
}
