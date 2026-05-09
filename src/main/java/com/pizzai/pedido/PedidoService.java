package com.pizzai.pedido;

import com.pizzai.cliente.Cliente;
import com.pizzai.cliente.ClienteService;
import com.pizzai.pedido.dto.ItemPedidoRequestDTO;
import com.pizzai.pedido.dto.PedidoRequestDTO;
import com.pizzai.pedido.dto.PedidoResponseDTO;
import com.pizzai.pizza.Pizza;
import com.pizzai.pizza.PizzaService;
import com.pizzai.shared.enums.StatusPedido;
import com.pizzai.shared.exception.BusinessException;
import com.pizzai.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Transactional
public class PedidoService {

    private static final DateTimeFormatter NUMERO_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final PedidoRepository repository;
    private final ClienteService clienteService;
    private final PizzaService pizzaService;

    public PedidoService(PedidoRepository repository,
                         ClienteService clienteService,
                         PizzaService pizzaService) {
        this.repository = repository;
        this.clienteService = clienteService;
        this.pizzaService = pizzaService;
    }

    public PedidoResponseDTO criar(PedidoRequestDTO dto) {
        Cliente cliente = clienteService.buscarEntidade(dto.clienteId());

        Pedido pedido = Pedido.builder()
                .numero(gerarNumero())
                .cliente(cliente)
                .status(StatusPedido.AGUARDANDO)
                .formaPagamento(dto.formaPagamento())
                .valorEntrega(dto.valorEntrega())
                .observacoes(dto.observacoes())
                .enderecoEntrega(dto.enderecoEntrega() != null
                        ? dto.enderecoEntrega().toEntity()
                        : cliente.getEndereco())
                .build();

        for (ItemPedidoRequestDTO itemDto : dto.itens()) {
            Pizza pizza = pizzaService.buscarEntidade(itemDto.pizzaId());
            if (!pizza.isDisponivel()) {
                throw new BusinessException("Pizza '" + pizza.getNome() + "' nao esta disponivel");
            }
            BigDecimal preco = pizza.calcularPreco();
            ItemPedido item = ItemPedido.builder()
                    .pizza(pizza)
                    .quantidade(itemDto.quantidade())
                    .precoUnitario(preco)
                    .build();
            item.calcularSubtotal();
            pedido.adicionarItem(item);
        }

        pedido.recalcularTotais();
        return PedidoResponseDTO.fromEntity(repository.save(pedido));
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarPorId(Long id) {
        return PedidoResponseDTO.fromEntity(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarPorNumero(String numero) {
        Pedido pedido = repository.findByNumero(numero)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", numero));
        return PedidoResponseDTO.fromEntity(pedido);
    }

    @Transactional(readOnly = true)
    public Page<PedidoResponseDTO> listar(Long clienteId, StatusPedido status, Pageable pageable) {
        Page<Pedido> pagina;
        if (clienteId != null) {
            pagina = repository.findByClienteId(clienteId, pageable);
        } else if (status != null) {
            pagina = repository.findByStatus(status, pageable);
        } else {
            pagina = repository.findAll(pageable);
        }
        return pagina.map(PedidoResponseDTO::fromEntity);
    }

    public PedidoResponseDTO atualizarStatus(Long id, StatusPedido novoStatus) {
        Pedido pedido = buscarEntidade(id);
        if (!pedido.getStatus().podeTransicionarPara(novoStatus)) {
            throw new BusinessException(String.format(
                    "Transicao de status invalida: %s -> %s",
                    pedido.getStatus(), novoStatus));
        }
        pedido.setStatus(novoStatus);
        return PedidoResponseDTO.fromEntity(repository.save(pedido));
    }

    public void cancelar(Long id) {
        Pedido pedido = buscarEntidade(id);
        if (pedido.getStatus().isFinalizado()) {
            throw new BusinessException("Pedidos finalizados nao podem ser cancelados");
        }
        pedido.setStatus(StatusPedido.CANCELADO);
        repository.save(pedido);
    }

    public Pedido buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));
    }

    private String gerarNumero() {
        String prefixo = "PZ-" + LocalDate.now().format(NUMERO_FORMATTER) + "-";
        for (int tentativas = 0; tentativas < 5; tentativas++) {
            String sufixo = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            String candidato = prefixo + sufixo;
            if (!repository.existsByNumero(candidato)) {
                return candidato;
            }
        }
        throw new BusinessException("Nao foi possivel gerar numero unico para o pedido");
    }
}
