package io.sim;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CriarPlanilhaExcel {

    public void createSheet(String nome) {
        String nomeDoArquivo = "exemplo.xlsx";

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(nome);

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Iteração");
            headerRow.createCell(1).setCellValue("Timestamp");
            headerRow.createCell(2).setCellValue("ID Car");
            headerRow.createCell(3).setCellValue("ID Route");
            headerRow.createCell(4).setCellValue("Speed");
            headerRow.createCell(5).setCellValue("Distance");
            headerRow.createCell(6).setCellValue("FuelConsumption");
            headerRow.createCell(7).setCellValue("FuelType");
            headerRow.createCell(8).setCellValue("CO2Emission");
            headerRow.createCell(9).setCellValue("posição X");
            headerRow.createCell(10).setCellValue("posição Y");

            // Salva o arquivo .xlsx após criar todas as abas de planilha.
            try (FileOutputStream outputStream = new FileOutputStream(nomeDoArquivo)) {
                workbook.write(outputStream);
                //System.out.println("Planilha criada com sucesso!");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateSheetCar(DrivingData data, String nomeDoArquivo, String nomeDaAba,int i, long tempoAtualMillis) {

        try (FileInputStream inputStream = new FileInputStream(nomeDoArquivo);
            Workbook workbook = WorkbookFactory.create(inputStream);
            FileOutputStream outputStream = new FileOutputStream(nomeDoArquivo)) {

            Sheet sheet = workbook.getSheet(nomeDaAba);

            int lastRowNum = sheet.getLastRowNum();
            Row newRow = sheet.createRow(lastRowNum + 1);

            // Preencha as células da nova linha com os dados da classe TransferData
            newRow.createCell(0).setCellValue(i);
            newRow.createCell(1).setCellValue(tempoAtualMillis);
            newRow.createCell(2).setCellValue(data.getAutoID()); //Pegar o ID do carro
            newRow.createCell(3).setCellValue(data.getRouteIDSUMO());
            newRow.createCell(4).setCellValue(data.getSpeed());
            newRow.createCell(5).setCellValue(data.getOdometer());
            newRow.createCell(6).setCellValue(data.getFuelConsumption());
            newRow.createCell(7).setCellValue(data.getFuelType());
            newRow.createCell(8).setCellValue(data.getCo2Emission());
            newRow.createCell(9).setCellValue(data.getX_Position()); // Corrigido para pegar a posição X
            newRow.createCell(10).setCellValue(data.getY_Position()); // Corrigido para pegar a posição Y

            // Salve as alterações na planilha
            workbook.write(outputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void adicionarAba(String nomeDoArquivo, String nomeDaAba) {
        try (FileInputStream inputStream = new FileInputStream(nomeDoArquivo);
            Workbook workbook = WorkbookFactory.create(inputStream);
            FileOutputStream outputStream = new FileOutputStream(nomeDoArquivo)) {

            // Cria uma nova aba na planilha existente
            Sheet newSheet = workbook.createSheet(nomeDaAba);

            // Adiciona o cabeçalho à nova aba
            Row headerRow = newSheet.createRow(0);
            headerRow.createCell(0).setCellValue("Timestamp");
            headerRow.createCell(1).setCellValue("ID Car");
            headerRow.createCell(2).setCellValue("ID Route");
            headerRow.createCell(3).setCellValue("Speed");
            headerRow.createCell(4).setCellValue("Distance");
            headerRow.createCell(5).setCellValue("FuelConsumption");
            headerRow.createCell(6).setCellValue("FuelType");
            headerRow.createCell(7).setCellValue("CO2Emission");
            headerRow.createCell(8).setCellValue("posição X");
            headerRow.createCell(9).setCellValue("posição Y");

            // Salva as alterações na planilha
            workbook.write(outputStream);
            System.out.println("Nova aba criada com sucesso!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pularLinha(String nomeDoArquivo, String nomeDaAba) {
        try (FileInputStream inputStream = new FileInputStream(nomeDoArquivo);
             Workbook workbook = WorkbookFactory.create(inputStream);
             FileOutputStream outputStream = new FileOutputStream(nomeDoArquivo)) {
    
            Sheet sheet = workbook.getSheet(nomeDaAba);
    
            int lastRowNum = sheet.getLastRowNum();
            // Pular uma linha
            sheet.createRow(lastRowNum + 1);
    
            // Salve as alterações na planilha
            workbook.write(outputStream);
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    public static void main(String[] args) {
       
    }
}
