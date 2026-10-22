package rojvsjoyeria.modelo;

public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private String material;
    private String categoria;
    private String imagenRuta;

    public Producto() {}

    public Producto(int id, String nombre, String descripcion, double precio,
                    int stock, String material, String categoria, String imagenRuta) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.material = material;
        this.categoria = categoria;
        this.imagenRuta = imagenRuta;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getImagenRuta() { return imagenRuta; }
    public void setImagenRuta(String imagenRuta) { this.imagenRuta = imagenRuta; }

    @Override
    public String toString() { return nombre; }
}
