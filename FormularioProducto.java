package rojvsjoyeria.vista;

import rojvsjoyeria.dao.ProductoDAO;
import rojvsjoyeria.modelo.Producto;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class FormularioProducto extends JDialog {

    private boolean guardado = false;
    private Producto productoActual;
    private ProductoDAO dao = new ProductoDAO();

    private JTextField txtNombre, txtPrecio, txtStock, txtMaterial, txtCategoria, txtImagen;
    private JTextArea txtDescripcion;
    private JLabel lblPreviewImagen;

    public FormularioProducto(Frame parent, Producto producto) {
        super(parent, producto == null ? "Agregar Producto" : "Editar Producto", true);
        this.productoActual = producto;
        setSize(520, 560);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 235, 210));

        add(crearFormulario(), BorderLayout.CENTER);
        add(crearBotones(), BorderLayout.SOUTH);

        if (producto != null) cargarDatos(producto);
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 235, 210));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombre      = new JTextField(25);
        txtPrecio      = new JTextField(25);
        txtStock       = new JTextField(25);
        txtMaterial    = new JTextField(25);
        txtCategoria   = new JTextField(25);
        txtDescripcion = new JTextArea(3, 25);
        txtDescripcion.setLineWrap(true);
        txtImagen      = new JTextField(18);
        txtImagen.setEditable(false);

        lblPreviewImagen = new JLabel("Sin imagen", SwingConstants.CENTER);
        lblPreviewImagen.setPreferredSize(new Dimension(100, 100));
        lblPreviewImagen.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55)));
        lblPreviewImagen.setBackground(Color.WHITE);
        lblPreviewImagen.setOpaque(true);

        JButton btnSelImg = new JButton("...");
        btnSelImg.addActionListener(e -> seleccionarImagen());

        String[] etiquetas = {"Nombre:", "Precio ($):", "Stock:", "Material:", "Categoria:", "Descripcion:"};
        JComponent[] campos = {txtNombre, txtPrecio, txtStock, txtMaterial, txtCategoria, new JScrollPane(txtDescripcion)};

        for (int i = 0; i < etiquetas.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            panel.add(new JLabel(etiquetas[i]), gbc);
            gbc.gridx = 1; gbc.weightx = 1;
            panel.add(campos[i], gbc);
        }

        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0;
        panel.add(new JLabel("Imagen:"), gbc);
        JPanel panelImg = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        panelImg.setBackground(new Color(245, 235, 210));
        panelImg.add(txtImagen);
        panelImg.add(btnSelImg);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(panelImg, gbc);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblPreviewImagen, gbc);

        return panel;
    }

    private JPanel crearBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(new Color(60, 30, 10));

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(34, 139, 34));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 13));
        btnGuardar.addActionListener(e -> guardar());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(180, 30, 30));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 13));
        btnCancelar.addActionListener(e -> dispose());

        panel.add(btnGuardar);
        panel.add(btnCancelar);
        return panel;
    }

    private void seleccionarImagen() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Imagenes (jpg, png, gif)", "jpg", "jpeg", "png", "gif"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            txtImagen.setText(f.getAbsolutePath());
            ImageIcon icon = new ImageIcon(new ImageIcon(f.getAbsolutePath())
                .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
            lblPreviewImagen.setIcon(icon);
            lblPreviewImagen.setText("");
        }
    }

    private void cargarDatos(Producto p) {
        txtNombre.setText(p.getNombre());
        txtPrecio.setText(String.valueOf(p.getPrecio()));
        txtStock.setText(String.valueOf(p.getStock()));
        txtMaterial.setText(p.getMaterial());
        txtCategoria.setText(p.getCategoria());
        txtDescripcion.setText(p.getDescripcion());
        if (p.getImagenRuta() != null) {
            txtImagen.setText(p.getImagenRuta());
            File f = new File(p.getImagenRuta());
            if (f.exists()) {
                ImageIcon icon = new ImageIcon(new ImageIcon(p.getImagenRuta())
                    .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                lblPreviewImagen.setIcon(icon);
                lblPreviewImagen.setText("");
            }
        }
    }

    private void guardar() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.");
            return;
        }
        try {
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());

            Producto p = productoActual != null ? productoActual : new Producto();
            p.setNombre(txtNombre.getText().trim());
            p.setDescripcion(txtDescripcion.getText().trim());
            p.setPrecio(precio);
            p.setStock(stock);
            p.setMaterial(txtMaterial.getText().trim());
            p.setCategoria(txtCategoria.getText().trim());
            p.setImagenRuta(txtImagen.getText().trim());

            boolean ok = productoActual == null ? dao.insertar(p) : dao.actualizar(p);
            if (ok) {
                guardado = true;
                JOptionPane.showMessageDialog(this, "Producto guardado correctamente.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el producto.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio y Stock deben ser numericos.");
        }
    }

    public boolean isGuardado() { return guardado; }
}
