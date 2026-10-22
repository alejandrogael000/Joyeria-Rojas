package rojvsjoyeria.vista;

import rojvsjoyeria.dao.ProductoDAO;
import rojvsjoyeria.modelo.Producto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private ProductoDAO dao = new ProductoDAO();
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private JTextField txtBuscar;
    private JLabel lblImagen;

    public VentanaPrincipal() {
        setTitle("Joyeria Rojvs - Sistema de Productos");
        setSize(1100, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));
        getContentPane().setBackground(new Color(30, 20, 10));

        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);

        cargarTabla(dao.listarTodos());
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panel.setBackground(new Color(60, 30, 10));

        JLabel logo = new JLabel("ROJVS JOYERIA DE PLATA");
        logo.setFont(new Font("Georgia", Font.BOLD, 22));
        logo.setForeground(new Color(212, 175, 55));

        txtBuscar = new JTextField(20);
        JButton btnBuscar = crearBoton("Buscar", new Color(100, 70, 20));
        btnBuscar.addActionListener(e -> buscar());

        JButton btnMostrarTodos = crearBoton("Mostrar Todos", new Color(80, 50, 10));
        btnMostrarTodos.addActionListener(e -> cargarTabla(dao.listarTodos()));

        panel.add(logo);
        panel.add(Box.createHorizontalStrut(30));
        panel.add(new JLabel("Buscar:") {{ setForeground(Color.WHITE); }});
        panel.add(txtBuscar);
        panel.add(btnBuscar);
        panel.add(btnMostrarTodos);
        return panel;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(30, 20, 10));

        String[] columnas = {"ID", "Nombre", "Descripcion", "Precio", "Stock", "Material", "Categoria"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(28);
        tabla.setFont(new Font("Arial", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(new Color(100, 70, 20));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setBackground(new Color(245, 235, 210));
        tabla.getSelectionModel().addListSelectionListener(e -> mostrarImagen());

        JScrollPane scroll = new JScrollPane(tabla);

        lblImagen = new JLabel("Sin imagen", SwingConstants.CENTER);
        lblImagen.setPreferredSize(new Dimension(200, 200));
        lblImagen.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2));
        lblImagen.setBackground(new Color(245, 235, 210));
        lblImagen.setOpaque(true);
        lblImagen.setFont(new Font("Arial", Font.ITALIC, 12));

        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBackground(new Color(30, 20, 10));
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));
        JLabel lblTitImg = new JLabel("Imagen del Producto", SwingConstants.CENTER);
        lblTitImg.setForeground(new Color(212, 175, 55));
        lblTitImg.setFont(new Font("Arial", Font.BOLD, 13));
        panelDerecho.add(lblTitImg, BorderLayout.NORTH);
        panelDerecho.add(lblImagen, BorderLayout.CENTER);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelDerecho, BorderLayout.EAST);
        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(new Color(60, 30, 10));

        JButton btnAgregar   = crearBoton("+ Agregar",   new Color(34, 139, 34));
        JButton btnModificar  = crearBoton("Editar",      new Color(30, 100, 180));
        JButton btnEliminar   = crearBoton("Eliminar",    new Color(180, 30, 30));
        JButton btnActualizar = crearBoton("Actualizar",  new Color(100, 70, 20));

        btnAgregar.addActionListener(e -> abrirFormulario(null));
        btnModificar.addActionListener(e -> editarSeleccionado());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        btnActualizar.addActionListener(e -> cargarTabla(dao.listarTodos()));

        panel.add(btnAgregar);
        panel.add(btnModificar);
        panel.add(btnEliminar);
        panel.add(btnActualizar);
        return panel;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        return btn;
    }

    private void cargarTabla(List<Producto> lista) {
        modeloTabla.setRowCount(0);
        for (Producto p : lista) {
            modeloTabla.addRow(new Object[]{
                p.getId(), p.getNombre(), p.getDescripcion(),
                String.format("$%.2f", p.getPrecio()),
                p.getStock(), p.getMaterial(), p.getCategoria()
            });
        }
        lblImagen.setIcon(null);
        lblImagen.setText("Sin imagen");
    }

    private void mostrarImagen() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        int id = (int) modeloTabla.getValueAt(fila, 0);
        Producto p = dao.buscarPorId(id);
        if (p != null && p.getImagenRuta() != null && !p.getImagenRuta().isEmpty()) {
            File f = new File(p.getImagenRuta());
            if (f.exists()) {
                ImageIcon icon = new ImageIcon(new ImageIcon(p.getImagenRuta())
                    .getImage().getScaledInstance(190, 190, Image.SCALE_SMOOTH));
                lblImagen.setIcon(icon);
                lblImagen.setText("");
                return;
            }
        }
        lblImagen.setIcon(null);
        lblImagen.setText("Sin imagen");
    }

    private void buscar() {
        String texto = txtBuscar.getText().trim().toLowerCase();
        if (texto.isEmpty()) { cargarTabla(dao.listarTodos()); return; }
        List<Producto> todos = dao.listarTodos();
        java.util.List<Producto> filtrados = new java.util.ArrayList<>();
        for (Producto p : todos) {
            if (p.getNombre().toLowerCase().contains(texto) ||
                p.getCategoria().toLowerCase().contains(texto) ||
                p.getMaterial().toLowerCase().contains(texto)) {
                filtrados.add(p);
            }
        }
        cargarTabla(filtrados);
    }

    private void abrirFormulario(Producto producto) {
        FormularioProducto form = new FormularioProducto(this, producto);
        form.setVisible(true);
        if (form.isGuardado()) cargarTabla(dao.listarTodos());
    }

    private void editarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un producto."); return; }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        abrirFormulario(dao.buscarPorId(id));
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un producto."); return; }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = (String) modeloTabla.getValueAt(fila, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Eliminar el producto: " + nombre + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "Producto eliminado.");
                cargarTabla(dao.listarTodos());
            }
        }
    }
}
