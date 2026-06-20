package com.utn.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(
        callSuper = false,
        onlyExplicitlyIncluded = false)
@Entity
@Table(name = "detalle_pedidos")
public class DetallePedido extends Base {

    private int cantidad;
    private Double subtotal;
    @EqualsAndHashCode.Include
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "producto_id")
        private Producto producto;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "pedido_id")
        @ToString.Exclude
        private Pedido pedido;

    public DetallePedido(int cantidad, Producto producto) {
        super();
        this.cantidad = cantidad;
        this.producto = producto;
        this.calcularSubtotal();
    }

    private void calcularSubtotal() {
        this.subtotal = producto.getPrecio() * this.cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal();
    }

}
