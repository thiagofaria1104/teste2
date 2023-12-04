package io.sim;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;

public class CriarPlanilhaExcelTest {

    @Test
    public void testCreateSheet() {
        CriarPlanilhaExcel criador = new CriarPlanilhaExcel();
        criador.createSheet("TestAba");

        // Verifica se o arquivo foi criado corretamente
        assertTrue("O arquivo deve existir", new File("exemplo.xlsx").exists());
    }
    @Test
    public void testAdicionarAba() {
        CriarPlanilhaExcel criador = new CriarPlanilhaExcel();
        criador.createSheet("TestAba");
        criador.adicionarAba("exemplo.xlsx", "NovaAba");

        // Obtém a última aba para verificar se foi adicionada corretamente
        Sheet newSheet = null;
        try (FileInputStream inputStream = new FileInputStream("exemplo.xlsx");
            Workbook workbook = WorkbookFactory.create(inputStream)) {
            newSheet = workbook.getSheet("NovaAba");
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertNotNull("A nova aba não deve ser nula", newSheet);
        assertEquals("Deve haver um cabeçalho na nova aba", "Timestamp",
        newSheet.getRow(0).getCell(0).getStringCellValue());

    }
}
