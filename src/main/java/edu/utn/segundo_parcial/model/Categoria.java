package edu.utn.segundo_parcial.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
@EqualsAndHashCode(
        callSuper = false,
        onlyExplicitlyIncluded = true)
@Entity
@Table(name = "categorias")
public class Categoria extends Base{

    @EqualsAndHashCode.Include
    @Column(name = "nombre", unique = true, nullable = false)
    private String nombre;
    private String descripcion;
    @Builder.Default
    @OneToMany(mappedBy = "categoria", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Producto> productos = new HashSet<>();

    public boolean addProducto(Producto producto) {
        if(this.productos.add(producto)) {
            producto.setCategoria(this);
            return true;
        } else {
            System.out.println("No se pudo agregar el producto " + producto.getNombre() + " porque ya existe en esta categoría.");
            return false;
        }
    }
}
