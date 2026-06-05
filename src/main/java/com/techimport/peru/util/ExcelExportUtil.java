package com.techimport.peru.util;

import com.techimport.peru.modelo.Usuario;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Clase utilitaria para la exportación de datos a archivos Excel (.xlsx).
 *
 * <p>Utiliza Apache POI para generar libros de trabajo Excel con formato profesional,
 * incluyendo encabezados estilizados, columnas auto-dimensionadas y colores alternados
 * en las filas para mejorar la legibilidad.</p>
 *
 * <p>Ejemplo de uso:</p>
 * <pre>{@code
 *   List<Usuario> usuarios = usuarioDAO.listarTodos();
 *   Workbook workbook = ExcelExportUtil.exportarUsuarios(usuarios);
 *   ExcelExportUtil.guardarArchivo(workbook, "C:/reportes/usuarios.xlsx");
 * }</pre>
 *
 * @author TechImport Perú
 * @version 1.0
 * @since 2026-06-07
 */
public final class ExcelExportUtil {

    /** Logger para registrar eventos de exportación de Excel. */
    private static final Logger logger = LoggerFactory.getLogger(ExcelExportUtil.class);

    /** Nombre de la hoja de cálculo para el reporte de usuarios. */
    private static final String NOMBRE_HOJA = "Usuarios";

    /** Títulos de las columnas del reporte de usuarios. */
    private static final String[] COLUMNAS_USUARIO = {
            "ID", "Nombres", "Apellidos", "Email", "Rol", "Fecha Creación", "Estado"
    };

    /**
     * Constructor privado para evitar la instanciación de esta clase utilitaria.
     */
    private ExcelExportUtil() {
        // Clase utilitaria — no instanciable
    }

    /**
     * Genera un libro de trabajo Excel (.xlsx) con la lista de usuarios proporcionada.
     *
     * <p>El archivo generado incluye:</p>
     * <ul>
     *   <li>Una fila de encabezado con fondo azul oscuro, texto blanco y fuente en negrita</li>
     *   <li>Columnas: ID, Nombres, Apellidos, Email, Rol, Fecha Creación, Estado</li>
     *   <li>Colores alternados en las filas de datos para mejorar la legibilidad</li>
     *   <li>Columnas auto-dimensionadas al contenido</li>
     * </ul>
     *
     * @param usuarios la lista de usuarios a exportar; no debe ser {@code null}
     * @return un {@link Workbook} con los datos de los usuarios formateados;
     *         nunca {@code null}
     * @throws IllegalArgumentException si la lista de usuarios es {@code null}
     */
    public static Workbook exportarUsuarios(List<Usuario> usuarios) {
        if (usuarios == null) {
            logger.error("Se intentó exportar una lista de usuarios nula.");
            throw new IllegalArgumentException("La lista de usuarios no puede ser nula.");
        }

        logger.info("Iniciando exportación de {} usuarios a Excel.", usuarios.size());

        Workbook workbook = new XSSFWorkbook();
        Sheet hoja = workbook.createSheet(NOMBRE_HOJA);

        // Crear estilo para el encabezado
        CellStyle estiloEncabezado = crearEstiloEncabezado(workbook);

        // Crear estilo para filas pares (color alternado)
        CellStyle estiloFilaPar = crearEstiloFilaPar(workbook);

        // Crear estilo para filas impares
        CellStyle estiloFilaImpar = crearEstiloFilaImpar(workbook);

        // Crear fila de encabezado
        Row filaEncabezado = hoja.createRow(0);
        for (int i = 0; i < COLUMNAS_USUARIO.length; i++) {
            Cell celda = filaEncabezado.createCell(i);
            celda.setCellValue(COLUMNAS_USUARIO[i]);
            celda.setCellStyle(estiloEncabezado);
        }

        // Llenar datos de usuarios
        int numeroFila = 1;
        for (Usuario usuario : usuarios) {
            Row fila = hoja.createRow(numeroFila);

            // Seleccionar estilo alternado
            CellStyle estiloFila = (numeroFila % 2 == 0) ? estiloFilaPar : estiloFilaImpar;

            // ID
            Cell celdaId = fila.createCell(0);
            celdaId.setCellValue(usuario.getId());
            celdaId.setCellStyle(estiloFila);

            // Nombres
            Cell celdaNombres = fila.createCell(1);
            celdaNombres.setCellValue(usuario.getNombres() != null ? usuario.getNombres() : "");
            celdaNombres.setCellStyle(estiloFila);

            // Apellidos
            Cell celdaApellidos = fila.createCell(2);
            celdaApellidos.setCellValue(usuario.getApellidos() != null ? usuario.getApellidos() : "");
            celdaApellidos.setCellStyle(estiloFila);

            // Email
            Cell celdaEmail = fila.createCell(3);
            celdaEmail.setCellValue(usuario.getEmail() != null ? usuario.getEmail() : "");
            celdaEmail.setCellStyle(estiloFila);

            // Rol
            Cell celdaRol = fila.createCell(4);
            celdaRol.setCellValue(usuario.getRol() != null ? usuario.getRol() : "");
            celdaRol.setCellStyle(estiloFila);

            // Fecha Creación
            Cell celdaFecha = fila.createCell(5);
            celdaFecha.setCellValue(
                    usuario.getFechaCreacion() != null ? usuario.getFechaCreacion().toString() : "N/A");
            celdaFecha.setCellStyle(estiloFila);

            // Estado
            Cell celdaEstado = fila.createCell(6);
            celdaEstado.setCellValue(usuario.isActivo() ? "Activo" : "Inactivo");
            celdaEstado.setCellStyle(estiloFila);

            numeroFila++;
        }

        // Auto-dimensionar todas las columnas al contenido
        for (int i = 0; i < COLUMNAS_USUARIO.length; i++) {
            hoja.autoSizeColumn(i);
        }

        logger.info("Exportación de usuarios a Excel completada exitosamente. Total filas: {}",
                usuarios.size());

        return workbook;
    }

    /**
     * Guarda un libro de trabajo Excel en el sistema de archivos.
     *
     * @param workbook     el libro de trabajo a guardar; no debe ser {@code null}
     * @param rutaArchivo  la ruta completa del archivo de destino (e.g., "C:/reportes/usuarios.xlsx");
     *                     no debe ser {@code null} ni estar vacía
     * @throws IllegalArgumentException si el workbook o la ruta son {@code null}
     */
    public static void guardarArchivo(Workbook workbook, String rutaArchivo) {
        if (workbook == null) {
            logger.error("Se intentó guardar un workbook nulo.");
            throw new IllegalArgumentException("El workbook no puede ser nulo.");
        }

        if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
            logger.error("Se intentó guardar un archivo con ruta vacía o nula.");
            throw new IllegalArgumentException("La ruta del archivo no puede ser nula ni estar vacía.");
        }

        logger.info("Guardando archivo Excel en: {}", rutaArchivo);

        try (FileOutputStream outputStream = new FileOutputStream(rutaArchivo)) {
            workbook.write(outputStream);
            logger.info("Archivo Excel guardado exitosamente en: {}", rutaArchivo);
        } catch (IOException e) {
            logger.error("Error al guardar el archivo Excel en {}: {}", rutaArchivo, e.getMessage(), e);
        } finally {
            try {
                workbook.close();
                logger.debug("Workbook cerrado exitosamente.");
            } catch (IOException e) {
                logger.error("Error al cerrar el workbook: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * Crea el estilo para la fila de encabezado del reporte.
     *
     * <p>Características del estilo:</p>
     * <ul>
     *   <li>Fondo azul oscuro ({@link IndexedColors#DARK_BLUE})</li>
     *   <li>Texto en color blanco</li>
     *   <li>Fuente en negrita, tamaño 12</li>
     *   <li>Alineación horizontal centrada</li>
     * </ul>
     *
     * @param workbook el libro de trabajo al cual pertenece el estilo
     * @return el {@link CellStyle} configurado para el encabezado
     */
    private static CellStyle crearEstiloEncabezado(Workbook workbook) {
        CellStyle estilo = workbook.createCellStyle();

        // Fondo azul oscuro
        estilo.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Fuente blanca y en negrita
        Font fuente = workbook.createFont();
        fuente.setColor(IndexedColors.WHITE.getIndex());
        fuente.setBold(true);
        fuente.setFontHeightInPoints((short) 12);
        fuente.setFontName("Arial");
        estilo.setFont(fuente);

        // Alineación centrada
        estilo.setAlignment(HorizontalAlignment.CENTER);

        // Bordes
        estilo.setBorderBottom(BorderStyle.THIN);
        estilo.setBorderTop(BorderStyle.THIN);
        estilo.setBorderLeft(BorderStyle.THIN);
        estilo.setBorderRight(BorderStyle.THIN);

        return estilo;
    }

    /**
     * Crea el estilo para las filas pares del reporte (color alternado).
     *
     * <p>Utiliza un fondo gris claro para diferenciar visualmente las filas.</p>
     *
     * @param workbook el libro de trabajo al cual pertenece el estilo
     * @return el {@link CellStyle} configurado para filas pares
     */
    private static CellStyle crearEstiloFilaPar(Workbook workbook) {
        CellStyle estilo = workbook.createCellStyle();
        estilo.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font fuente = workbook.createFont();
        fuente.setFontName("Arial");
        fuente.setFontHeightInPoints((short) 10);
        estilo.setFont(fuente);

        estilo.setBorderBottom(BorderStyle.THIN);
        estilo.setBorderTop(BorderStyle.THIN);
        estilo.setBorderLeft(BorderStyle.THIN);
        estilo.setBorderRight(BorderStyle.THIN);

        return estilo;
    }

    /**
     * Crea el estilo para las filas impares del reporte.
     *
     * <p>Utiliza fondo blanco para alternar con las filas pares.</p>
     *
     * @param workbook el libro de trabajo al cual pertenece el estilo
     * @return el {@link CellStyle} configurado para filas impares
     */
    private static CellStyle crearEstiloFilaImpar(Workbook workbook) {
        CellStyle estilo = workbook.createCellStyle();
        estilo.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font fuente = workbook.createFont();
        fuente.setFontName("Arial");
        fuente.setFontHeightInPoints((short) 10);
        estilo.setFont(fuente);

        estilo.setBorderBottom(BorderStyle.THIN);
        estilo.setBorderTop(BorderStyle.THIN);
        estilo.setBorderLeft(BorderStyle.THIN);
        estilo.setBorderRight(BorderStyle.THIN);

        return estilo;
    }
}
