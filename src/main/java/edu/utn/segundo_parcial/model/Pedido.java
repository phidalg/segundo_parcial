package com.utn.entities;

import com.utn.enums.Estado;
import com.utn.enums.FormaPago;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "pedidos")
public class Pedido extends Base implements Calculable {

    @Builder.Default
    private Estado estado = Estado.PENDIENTE;
    @Builder.Default
    @Setter(AccessLevel.NONE)
    private Double total = 0.0;
    private LocalDate fecha;
    private FormaPago formaPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @ToString.Exclude
    private Usuario usuario;

    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<DetallePedido> detalles = new HashSet<>();

    public abstract static class PedidoBuilder<C extends Pedido, B extends PedidoBuilder<C, B>>
            extends Base.BaseBuilder<C, B> {

        public B total(Double ignored) {
            return self();
        }

        public B estado(Estado ignored) {
            return self();
        }
    }

    public void addDetallePedido(int cantidad, Producto producto) {
        DetallePedido nuevoDetalle = new DetallePedido(cantidad, producto);
        nuevoDetalle.setPedido(this);
        if (this.detalles.add(nuevoDetalle)) {
            calcularTotal();
        } else {
            System.out.println(
                    "No se puede agregar un detalle de pedido para el producto " + producto.getNombre() +
                    " porque ya existe un detalle del producto referido en este mismo pedido.");
        }
    }

    public Optional<DetallePedido> findDetallePedidoByProducto(Producto producto) {
        return detalles.stream()
                .filter(d -> d.getProducto().equals(producto))
                .findFirst();
    }

    public void deleteDetallePedidoByProducto(Producto producto) {
        findDetallePedidoByProducto(producto)
                .ifPresent(detalle -> {
                    detalles.remove(detalle);
                    calcularTotal();
                });
    }

    @Override
    public void calcularTotal() {
        this.total = detalles.stream()
                .mapToDouble(DetallePedido::getSubtotal)
                .sum();
    }
}