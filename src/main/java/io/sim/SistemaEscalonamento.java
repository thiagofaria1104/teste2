package io.sim;

import java.util.Random;
import java.util.concurrent.*;

public class SistemaEscalonamento {

    //private long lastArrivalTimeMetodo1 = 0;
    private long periodoMetodo1 = 1000; // Período em milissegundos (1 segundo)
    private long deadlineMetodo1 = -1;

    //private long lastArrivalTimeMetodo2;
    private long periodoMetodo2 = 1500; // Período em milissegundos (1,5 segundos)
    private long deadlineMetodo2 = -1;

    //private long lastArrivalTimeMetodo3;
    private long periodoMetodo3 = 2000; // Período em milissegundos (2 segundos)
    private long deadlineMetodo3 = -1;

    //private long lastArrivalTimeMetodo4;
    private long periodoMetodo4 = 2500; // Período em milissegundos (2,5 segundos)
    private long deadlineMetodo4 = -1;

    //private long lastArrivalTimeMetodo5;
    private long periodoMetodo5 = 3000; // Período em milissegundos (3 segundos)
    private long deadlineMetodo5 = -1;

    //private long lastArrivalTimeMetodo6;
    private long periodoMetodo6 = 3500; // Período em milissegundos (3,5 segundos)
    private long deadlineMetodo6 = -1;

    //private long lastArrivalTimeMetodo7;
    private long periodoMetodo7 = 4000; // Período em milissegundos (4 segundos)
    private long deadlineMetodo7 = -1;

    //private long lastArrivalTimeMetodo8;
    private long periodoMetodo8 = 4500; // Período em milissegundos (4,5 segundos)
    private long deadlineMetodo8 = -1;

    //private long lastArrivalTimeMetodo9;
    private long periodoMetodo9 = 5000; // Período em milissegundos (5 segundos)
    private long deadlineMetodo9 = -1;

    //private long lastArrivalTimeMetodo10;
    private long periodoMetodo10 = 5500; // Período em milissegundos (5,5 segundos)
    private long deadlineMetodo10 = -1;

    private long tempoInicioSimulacao;

    public static void main(String[] args) {

        SistemaEscalonamento sistema = new SistemaEscalonamento();
        sistema.iniciarSimulacao();
        sistema.executarTarefas();
        
    }

    public void metodo1() {

        // Momento em que uma tarefa chega ao sistema ou ao escalonador para ser
        // executada
        long arrivalTime = obterTempoSimulacaoSegundos();
        System.out.println("Método 1: Tempo de chegada - " + arrivalTime);

        // Momento em que uma tarefa está pronta para ser executada e é liberada para o
        // escalonador
        long releaseTime = obterTempoSimulacaoSegundos();
        System.out.println("Método 1: Release Time - " + releaseTime);

        long startTime = obterTempoSimulacaoSegundos();
        // Lógica da tarefa
        Random random = new Random();
        int processamento = random.nextInt(500) + 500; // Tempo de processamento entre 500 e 1000 milissegundos
        try {
            Thread.sleep(processamento);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Tempo total que uma tarefa leva para ser executada, desde o momento em que é
        // liberada até a conclusão da execução
        long endTime = obterTempoSimulacaoSegundos();
        long computationTime = endTime - startTime;
        System.out.println("Método 1: Tempo de Computação - " + computationTime + " ms");

        // Jitter
        long jitter = releaseTime - arrivalTime;
        System.out.println("Método 1: Jitter - " + jitter + " ms");

        // Deadline como 90% do período
        deadlineMetodo1 = (long) (0.9 * periodoMetodo1);
        System.out.println("Método 1: Deadline - " + deadlineMetodo1);
    }

    public void metodo2() {
        // Momento em que uma tarefa chega ao sistema ou ao escalonador para ser
        // executada
        long arrivalTime = obterTempoSimulacaoSegundos();
        System.out.println("Método 2: Tempo de chegada - " + arrivalTime);

        // Momento em que uma tarefa está pronta para ser executada e é liberada para o
        // escalonador
        long releaseTime = obterTempoSimulacaoSegundos();
        System.out.println("Método 2: Release Time - " + releaseTime);

        long startTime = obterTempoSimulacaoSegundos();

        // Lógica da tarefa
        Random random = new Random();
        int processamento = random.nextInt(700) + 300; // Tempo de processamento entre 300 e 1000 milissegundos
        try {
            Thread.sleep(processamento);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Tempo total que uma tarefa leva para ser executada, desde o momento em que é
        // liberada até a conclusão da execução
        long endTime = obterTempoSimulacaoSegundos();
        long computationTime = endTime - startTime;
        System.out.println("Método 2: Tempo de Computação - " + computationTime + " ms");

        // Jitter 
        long jitter = releaseTime - arrivalTime;
        System.out.println("Método 2: Jitter - " + jitter + " ms");

        // Deadline como 90% do período (exemplo)
        deadlineMetodo2 = (long) (0.9 * periodoMetodo2);
        System.out.println("Método 2: Deadline - " + deadlineMetodo2);

    }

    public void metodo3() {
        // Momento em que uma tarefa chega ao sistema ou ao escalonador para ser
        // executada
        long arrivalTime = obterTempoSimulacaoSegundos();
        System.out.println("Método 3: Tempo de chegada - " + arrivalTime);

        // Momento em que uma tarefa está pronta para ser executada e é liberada para o
        // escalonador
        long releaseTime = obterTempoSimulacaoSegundos();
        System.out.println("Método 3: Release Time - " + releaseTime);

        long startTime = obterTempoSimulacaoSegundos();
        // Lógica da tarefa
        Random random = new Random();
        int processamento = random.nextInt(800) + 200; // Tempo de processamento entre 200 e 1000 milissegundos
        try {
            Thread.sleep(processamento);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Tempo total que uma tarefa leva para ser executada, desde o momento em que é
        // liberada até a conclusão da execução
        long endTime = obterTempoSimulacaoSegundos();
        long computationTime = endTime - startTime;
        System.out.println("Método 3: Tempo de Computação - " + computationTime + " ms");

        // Jitter
        long jitter = releaseTime - arrivalTime;
        System.out.println("Método 3: Jitter - " + jitter + " ms");

        // Deadline como 90% do período (exemplo)
        deadlineMetodo3 =  (long) (0.9 * periodoMetodo3);
        System.out.println("Método 3: Deadline - " + deadlineMetodo3);

    }

    public void metodo4() {
        // Momento em que uma tarefa chega ao sistema ou ao escalonador para ser
        // executada
        long arrivalTime = obterTempoSimulacaoSegundos();
        System.out.println("Método 4: Tempo de chegada - " + arrivalTime);

        // Momento em que uma tarefa está pronta para ser executada e é liberada para o
        // escalonador
        long releaseTime = obterTempoSimulacaoSegundos();
        System.out.println("Método 4: Release Time - " + releaseTime);

        long startTime = obterTempoSimulacaoSegundos();

        // Lógica da tarefa
        Random random = new Random();
        int processamento = random.nextInt(900) + 100; // Tempo de processamento entre 100 e 1000 milissegundos
        try {
            Thread.sleep(processamento);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Tempo total que uma tarefa leva para ser executada, desde o momento em que é
        // liberada até a conclusão da execução
        long endTime = obterTempoSimulacaoSegundos();
        long computationTime = endTime - startTime;
        System.out.println("Método 4: Tempo de Computação - " + computationTime + " ms");

        // Jitter
        long jitter = releaseTime - arrivalTime;
        System.out.println("Método 4: Jitter - " + jitter + " ms");

        // Deadline como 80% do período (exemplo)
        deadlineMetodo4 = (long) (0.8 * periodoMetodo4);
        System.out.println("Método 4: Deadline - " + deadlineMetodo4);
    }

    public void metodo5() {
        // Momento em que uma tarefa chega ao sistema ou ao escalonador para ser
        // executada
        long arrivalTime = obterTempoSimulacaoSegundos();
        System.out.println("Método 5: Tempo de chegada - " + arrivalTime);

        // Momento em que uma tarefa está pronta para ser executada e é liberada para o
        // escalonador
        long releaseTime = obterTempoSimulacaoSegundos();
        System.out.println("Método 5: Release Time - " + releaseTime);

        long startTime = obterTempoSimulacaoSegundos();

        // Lógica da tarefa
        Random random = new Random();
        int processamento = random.nextInt(1200) + 800; // Tempo de processamento entre 800 e 2000 milissegundos
        try {
            Thread.sleep(processamento);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Tempo total que uma tarefa leva para ser executada, desde o momento em que é
        // liberada até a conclusão da execução
        long endTime = obterTempoSimulacaoSegundos();
        long computationTime = endTime - startTime;
        System.out.println("Método 5: Tempo de Computação - " + computationTime + " ms");

        // Jitter como a variação entre o tempo de liberação e o tempo de chegada
        long jitter = releaseTime - arrivalTime;
        System.out.println("Método 5: Jitter - " + jitter + " ms");

        // Deadline como 95% do período (exemplo)
        deadlineMetodo5 = (long) (0.95 * periodoMetodo5);
        System.out.println("Método 5: Deadline - " + deadlineMetodo5);

    }

    public void metodo6() {
        // Momento em que uma tarefa chega ao sistema ou ao escalonador para ser
        // executada
        long arrivalTime = obterTempoSimulacaoSegundos();
        System.out.println("Método 6: Tempo de chegada - " + arrivalTime);

        // Momento em que uma tarefa está pronta para ser executada e é liberada para o
        // escalonador
        long releaseTime = obterTempoSimulacaoSegundos();
        System.out.println("Método 6: Release Time - " + releaseTime);

        long startTime = obterTempoSimulacaoSegundos();

        // Lógica da tarefa
        Random random = new Random();
        int processamento = random.nextInt(1500) + 1000; // Tempo de processamento entre 1000 e 2500 milissegundos
        try {
            Thread.sleep(processamento);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Tempo total que uma tarefa leva para ser executada, desde o momento em que é
        // liberada até a conclusão da execução
        long endTime = obterTempoSimulacaoSegundos();
        long computationTime = endTime - startTime;
        System.out.println("Método 6: Tempo de Computação - " + computationTime + " ms");

        // Jitter
        long jitter = releaseTime - arrivalTime;
        System.out.println("Método 6: Jitter - " + jitter + " ms");

        // Deadline como 85% do período (exemplo)
        deadlineMetodo6 = (long) (0.85 * periodoMetodo6);
        System.out.println("Método 6: Deadline - " + deadlineMetodo6);
    }

    public void metodo7() {
        // Momento em que uma tarefa chega ao sistema ou ao escalonador para ser
        // executada
        long arrivalTime = obterTempoSimulacaoSegundos();
        System.out.println("Método 7: Tempo de chegada - " + arrivalTime);

        // Momento em que uma tarefa está pronta para ser executada e é liberada para o
        // escalonador
        long releaseTime = obterTempoSimulacaoSegundos();
        System.out.println("Método 7: Release Time - " + releaseTime);

        long startTime = obterTempoSimulacaoSegundos();

        // Lógica da tarefa
        Random random = new Random();
        int processamento = random.nextInt(1800) + 1200; // Tempo de processamento entre 1200 e 3000 milissegundos
        try {
            Thread.sleep(processamento);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Tempo total que uma tarefa leva para ser executada, desde o momento em que é
        // liberada até a conclusão da execução
        long endTime = obterTempoSimulacaoSegundos();
        long computationTime = endTime - startTime;
        System.out.println("Método 7: Tempo de Computação - " + computationTime + " ms");

        // Jitter
        long jitter = releaseTime - arrivalTime;
        System.out.println("Método 7: Jitter - " + jitter + " ms");

        // Deadline como 80% do período (exemplo)
        deadlineMetodo7 = (long) (0.8 * periodoMetodo7);
        System.out.println("Método 7: Deadline - " + deadlineMetodo7);

    }

    public void metodo8() {
        // Momento em que uma tarefa chega ao sistema ou ao escalonador para ser
        // executada
        long arrivalTime = obterTempoSimulacaoSegundos();
        System.out.println("Método 8: Tempo de chegada - " + arrivalTime);

        
        // Momento em que uma tarefa está pronta para ser executada e é liberada para o
        // escalonador
        long releaseTime = obterTempoSimulacaoSegundos();
        System.out.println("Método 8: Release Time - " + releaseTime);

        long startTime = obterTempoSimulacaoSegundos();

        // Lógica da tarefa
        Random random = new Random();
        int processamento = random.nextInt(2000) + 1500; // Tempo de processamento entre 1500 e 3500 milissegundos
        try {
            Thread.sleep(processamento);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Tempo total que uma tarefa leva para ser executada, desde o momento em que é
        // liberada até a conclusão da execução
        long endTime = obterTempoSimulacaoSegundos();
        long computationTime = endTime - startTime;
        System.out.println("Método 8: Tempo de Computação - " + computationTime + " ms");

        // Jitter como a variação entre o tempo de liberação e o tempo de chegada
        long jitter = releaseTime - arrivalTime;
        System.out.println("Método 8: Jitter - " + jitter + " ms");

        // Deadline como 90% do período (exemplo)
        deadlineMetodo8 =(long) (0.9 * periodoMetodo8);
        System.out.println("Método 8: Deadline - " + deadlineMetodo8);

    }

    public void metodo9() {
        // Momento em que uma tarefa chega ao sistema ou ao escalonador para ser
        // executada
        long arrivalTime = obterTempoSimulacaoSegundos();
        System.out.println("Método 9: Tempo de chegada - " + arrivalTime);

        // Momento em que uma tarefa está pronta para ser executada e é liberada para o
        // escalonador
        long releaseTime = obterTempoSimulacaoSegundos();
        System.out.println("Método 9: Release Time - " + releaseTime);

        long startTime = obterTempoSimulacaoSegundos();

        // Lógica da tarefa
        Random random = new Random();
        int processamento = random.nextInt(2500) + 2000; // Tempo de processamento entre 2000 e 4500 milissegundos
        try {
            Thread.sleep(processamento);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Tempo total que uma tarefa leva para ser executada, desde o momento em que é
        // liberada até a conclusão da execução
        long endTime = obterTempoSimulacaoSegundos();
        long computationTime = endTime - startTime;
        System.out.println("Método 9: Tempo de Computação - " + computationTime + " ms");

        // Jitter como a variação entre o tempo de liberação e o tempo de chegada
        long jitter = releaseTime - arrivalTime;
        System.out.println("Método 9: Jitter - " + jitter + " ms");

        // Deadline como 85% do período (exemplo)
        deadlineMetodo9 = (long) (0.85 * periodoMetodo9);
        System.out.println("Método 9: Deadline - " + deadlineMetodo9);

    }

    public void metodo10() {
        // Momento em que uma tarefa chega ao sistema ou ao escalonador para ser
        // executada
        long arrivalTime = obterTempoSimulacaoSegundos();
        System.out.println("Método 10: Tempo de chegada - " + arrivalTime);

        // Momento em que uma tarefa está pronta para ser executada e é liberada para o
        // escalonador
        long releaseTime = obterTempoSimulacaoSegundos();
        System.out.println("Método 10: Release Time - " + releaseTime);

        long startTime = obterTempoSimulacaoSegundos();

        // Lógica da tarefa
        Random random = new Random();
        int processamento = random.nextInt(3000) + 2500; // Tempo de processamento entre 2500 e 5500 milissegundos
        try {
            Thread.sleep(processamento);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Tempo total que uma tarefa leva para ser executada, desde o momento em que é
        // liberada até a conclusão da execução
        long endTime = obterTempoSimulacaoSegundos();
        long computationTime = endTime - startTime;
        System.out.println("Método 10: Tempo de Computação - " + computationTime + " ms");

        // Jitter
        long jitter = releaseTime - arrivalTime;
        System.out.println("Método 10: Jitter - " + jitter + " ms");

        // Deadline como 80% do período (exemplo)
        deadlineMetodo10 = (long) (0.8 * periodoMetodo10);
        System.out.println("Método 10: Deadline - " + deadlineMetodo10);

    }

    public void executarTarefas() {

        try (ExecutorService executor = new ThreadPoolExecutor(
                10, 10, 0L, TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue<>(), // Usando PriorityBlockingQueue para fila de prioridades
                new PriorityHandler()
        )) {
            long startTime = System.currentTimeMillis(); // Tempo inicial

            Future<?> future1 = executor.submit(new Tarefa(() -> metodo1(), 1));
            Future<?> future2 = executor.submit(new Tarefa(() -> metodo2(), 2));
            Future<?> future3 = executor.submit(new Tarefa(() -> metodo3(), 3));
            Future<?> future4 = executor.submit(new Tarefa(() -> metodo4(), 4));
            Future<?> future5 = executor.submit(new Tarefa(() -> metodo5(), 5));
            Future<?> future6 = executor.submit(new Tarefa(() -> metodo6(), 6));
            Future<?> future7 = executor.submit(new Tarefa(() -> metodo7(), 7));
            Future<?> future8 = executor.submit(new Tarefa(() -> metodo8(), 8));
            Future<?> future9 = executor.submit(new Tarefa(() -> metodo9(), 9));
            Future<?> future10 = executor.submit(new Tarefa(() -> metodo10(), 10));

            // Definindo dependências
            try {
                future1.get(); // Espera até que o método1 seja concluído antes de prosseguir
                future2.get(); // Espera até que o método2 seja concluído antes de prosseguir
                future3.get(); // Espera até que o método3 seja concluído antes de prosseguir
                future4.get(); // Espera até que o método4 seja concluído antes de prosseguir
                future5.get(); // Espera até que o método5 seja concluído antes de prosseguir
                future6.get(); // Espera até que o método6 seja concluído antes de prosseguir
                future7.get(); // Espera até que o método7 seja concluído antes de prosseguir
                future8.get(); // Espera até que o método8 seja concluído antes de prosseguir
                future9.get(); // Espera até que o método9 seja concluído antes de prosseguir
                future10.get();// Espera até que o método10 seja concluído antes de prosseguir

                // Tempo total de execução
                long endTime = System.currentTimeMillis();
                System.out.println("Tempo total de execução: " + (endTime - startTime) + " ms");

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } finally {
                executor.shutdown();
            }
        }
    }

    public void iniciarSimulacao() {
        tempoInicioSimulacao = System.currentTimeMillis();
    }

    public long obterTempoSimulacaoSegundos() {
        long tempoAtualMillis = System.currentTimeMillis();
        long tempoSimulacaoMillis = tempoAtualMillis - tempoInicioSimulacao;
        return tempoSimulacaoMillis; // milliseconds
    }

    // Comparador de prioridade para definir prioridades de tarefas
    static class PriorityHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            if (r instanceof ComparableFutureTask) {
                ComparableFutureTask<?> task = (ComparableFutureTask<?>) r;
                executor.getQueue().add(task); // Adiciona a tarefa à fila de espera com base na prioridade
            }
        }
    }

    // Classe que permite definir a prioridade de uma tarefa
    static class ComparableFutureTask<T> extends FutureTask<T> implements Comparable<ComparableFutureTask<T>> {
        private final int priority;

        public ComparableFutureTask(Callable<T> callable, int priority) {
            super(callable);
            this.priority = priority;
        }

        @Override
        public int compareTo(ComparableFutureTask<T> o) {
            return Integer.compare(priority, o.priority);
        }
    }

    static class Tarefa implements Callable<Object>, Comparable<Tarefa> {
        private final Runnable runnable;
        private final int priority;

        public Tarefa(Runnable runnable, int priority) {
            this.runnable = runnable;
            this.priority = priority;
        }

        @Override
        public Object call() throws Exception {
            // Executar lógica da tarefa
            runnable.run();
            return null;
        }

        public int getPriority() {
            return priority;
        }

        @Override
        public int compareTo(Tarefa o) {
            return Integer.compare(priority, o.priority);
        }
    }
}
