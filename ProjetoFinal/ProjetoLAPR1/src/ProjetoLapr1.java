import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.AbstractPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.terminal.FileTerminal;
import com.panayotis.gnuplot.terminal.GNUPlotTerminal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author Ases
 */
public class ProjetoLapr1 {

    static Scanner tec = new Scanner(System.in);
    static int LINHAS = 50000;

    public static void main(String[] args) throws FileNotFoundException {
        String ficheiro, resolucao = "Null", modelo = "Null", tipoOrdenacao = "Null", parModelo = "Null", momentoPrevisao, title, escolha = "";
        int n = 0, s = args.length;
        String[] dados = new String[3];

        if (s >= 2) {
            ficheiro = args[1];

            String[][] data_horas = new String[LINHAS][2];
            int[] energia = new int[LINHAS];
            int posicao, opcao = 99, qtd, qtd_aux, X_aux = 0, aux = 1, ult_aux = 1;
            double nAlpha_aux = 0;
            String data_aux1 = "";

            posicao = Leitura(data_horas, energia, ficheiro);
            int[] energia_aux = new int[posicao];
            int[] energia_media = new int[posicao];
            String[] datasHoras_aux = new String[posicao];

            if (s == 2) {
                while (opcao != 0) {
                    System.out.println();
                    System.out.println("============================================================MENU============================================================");
                    System.out.println();
                    System.out.println("OPÇÃO 1:  Visualisar gráfico dos consumos.");
                    System.out.println("OPÇÃO 2:  Visualisar média global e distribuição de observações.");
                    System.out.println("OPÇÃO 3:  Ordenar valores.");
                    System.out.println("OPÇÃO 4:  Efetuar filtragens.");
                    System.out.println("OPÇÃO 5:  Efetuar uma previsão.");
                    System.out.println("OPÇÃO 6:  Escolher outro ficheiro.");
                    System.out.println("OPÇÃO 0:  Fechar o programa!");
                    System.out.println();
                    System.out.print("Insira a opção que pretende: ");
                    opcao = tec.nextInt();

                    switch (opcao) {
                        case 1:
                            Submenu();
                            opcao = tec.nextInt();
                            qtd = AnalisarSeries(data_horas, energia, posicao, energia_aux, datasHoras_aux, n, X_aux, aux, opcao);
                            title = "AnalisarSeries_" + escolha;
                            Grafico(energia_aux, qtd, n, title);
                            break;

                        case 2:
                            Submenu();
                            opcao = tec.nextInt();
                            qtd = AnalisarSeries(data_horas, energia, posicao, energia_aux, datasHoras_aux, n, X_aux, aux, opcao);
                            Observacoes(energia_aux, qtd, dados, n);
                            break;

                        case 3:
                            Submenu();
                            opcao = tec.nextInt();
                            qtd = AnalisarSeries(data_horas, energia, posicao, energia_aux, datasHoras_aux, n, X_aux, aux, opcao);
                            Submenu2();
                            OrdenarMergeSort(energia_aux, qtd, n, X_aux);
                            title = "OrdenarMergeSort_" + escolha;
                            Grafico(energia_aux, qtd, n, title);
                            break;

                        case 4:
                            Submenu();
                            opcao = tec.nextInt();
                            qtd = AnalisarSeries(data_horas, energia, posicao, energia_aux, datasHoras_aux, n, X_aux, aux, opcao);
                            Submenu3();
                            title = "EscolherMedia" + escolha;
                            qtd_aux = EscolherMedia(energia_aux, energia_media, qtd, n, X_aux, nAlpha_aux, title);
                            CalcularErro(energia_aux, energia_media, qtd_aux, dados, n);
                            break;

                        case 5:
                            Submenu();
                            opcao = tec.nextInt();
                            qtd = AnalisarSeries(data_horas, energia, posicao, energia_aux, datasHoras_aux, n, X_aux, aux, opcao);
                            Previsao(energia_aux, qtd, data_horas, posicao, datasHoras_aux, opcao, n, ult_aux, data_aux1, X_aux, dados, 0);
                            break;

                        case 6:
                            System.out.println();
                            System.out.println("============================================================================================================================");
                            System.out.println();
                            System.out.println("Insira o nome do novo ficheiro.");
                            System.out.print("NOME: ");
                            posicao = Novo(data_horas, energia);
                            break;

                        case 0:
                            System.out.println();
                            System.out.println("============================================================================================================================");
                            System.out.println();
                            System.out.println("Fechando o programa...");
                            System.out.println();
                            System.out.println("============================================================================================================================");
                            System.out.println();
                            break;

                        default:
                            System.out.println();
                            System.out.println("============================================================================================================================");
                            System.out.println();
                            System.out.println("Opção inválida, tente novamente...");
                            break;
                    }
                }
            }

            if (s == 12) {

                if ("11".equals(args[3]) || "12".equals(args[3]) || "13".equals(args[3]) || "14".equals(args[3]) || "2".equals(args[3]) || "3".equals(args[3]) || "4".equals(args[3])) {
                    resolucao = args[3];
                    n++;
                }

                if ("1".equals(args[5]) || "2".equals(args[5])) {
                    modelo = args[5];
                    n++;
                }

                if ("1".equals(args[7]) || "2".equals(args[7])) {
                    tipoOrdenacao = args[7];
                    n++;
                }

                double x = Double.parseDouble(args[9]);
                if (x > 0 && x <= 1 && "2".equals(modelo)) {
                    parModelo = args[9];
                    n++;
                }
                if (x > 1 && x < posicao && "1".equals(modelo)) {
                    parModelo = args[9];
                    n++;
                }

                boolean veri = true;
                int num = Integer.parseInt(args[11]);
                String[] data = new String[3];
                momentoPrevisao = args[11];

                if (num > 9999999 && num < 100000000) {
                    data[0] = momentoPrevisao.substring(4, 8);
                    data[1] = momentoPrevisao.substring(2, 4);
                    data[2] = momentoPrevisao.substring(0, 2);

                    String ult = data_horas[posicao - 1][0];
                    String[] ocasioesFinais = ult.split("-");
                    String anoUlt = Integer.toString(Integer.parseInt(ocasioesFinais[0]) + 1), mesUlt = Integer.toString(Integer.parseInt(ocasioesFinais[1]) + 1), diaUlt = Integer.toString(Integer.parseInt(ocasioesFinais[2]) + 1);

                    if ("13".equals(mesUlt)) {
                        mesUlt = "1";
                    }

                    if (("1".equals(mesUlt) || "3".equals(mesUlt) || "5".equals(mesUlt) || "7".equals(mesUlt) || "8".equals(mesUlt) || "10".equals(mesUlt) || "12".equals(mesUlt)) && "32".equals(diaUlt)) {
                        mesUlt = Integer.toString(Integer.parseInt(mesUlt) + 1);

                        if ("13".equals(mesUlt)) {
                            mesUlt = "1";
                        }

                        diaUlt = "1";
                    }

                    if (("4".equals(mesUlt) || "6".equals(mesUlt) || "9".equals(mesUlt) || "11".equals(mesUlt)) && "32".equals(diaUlt)) {
                        mesUlt = Integer.toString(Integer.parseInt(mesUlt) + 1);
                        diaUlt = "1";
                    }

                    if ("2".equals(mesUlt)) {
                        if ("29".equals(diaUlt) && Integer.parseInt(anoUlt) % 4 != 0) {
                            mesUlt = Integer.toString(Integer.parseInt(mesUlt) + 1);
                            diaUlt = "1";
                        }
                        if ("30".equals(diaUlt) && Integer.parseInt(anoUlt) % 4 == 0) {
                            mesUlt = Integer.toString(Integer.parseInt(mesUlt) + 1);
                            diaUlt = "1";
                        }
                    }

                    if ("4".equals(args[3])) {
                        for (int i = 0; i < posicao; i++) {
                            String d = data_horas[i][0];
                            String[] ocasioes = d.split("-");
                            String ano = ocasioes[0];

                            if (veri) {
                                if (data[0].equals(ano)) {
                                    veri = false;
                                    n++;
                                }
                                if (data[0].equals(anoUlt)) {
                                    veri = false;
                                    n++;
                                    ult_aux++;
                                }
                            }
                        }
                    }

                    if ("3".equals(args[3])) {
                        for (int i = 0; i < posicao; i++) {
                            String d = data_horas[i][0];
                            String[] ocasioes = d.split("-");
                            String ano = ocasioes[0], mes = ocasioes[1];

                            if (veri) {
                                if (data[0].equals(ano) && data[1].equals(mes)) {
                                    veri = false;
                                    n++;
                                }
                                if (data[0].equals(anoUlt) && data[1].equals(mesUlt)) {
                                    veri = false;
                                    n++;
                                    ult_aux++;
                                }
                            }
                        }
                    }

                    if ("11".equals(args[3]) || "12".equals(args[3]) || "13".equals(args[3]) || "14".equals(args[3]) || "2".equals(args[3])) {
                        for (int i = 0; i < posicao; i++) {
                            String d = data_horas[i][0];
                            String[] ocasioes = d.split("-");
                            String ano = ocasioes[0], mes = ocasioes[1], dia = ocasioes[2];

                            if (veri) {
                                if (data[0].equals(ano) && data[1].equals(mes) && data[2].equals(dia)) {
                                    veri = false;
                                    n++;
                                }
                                if (data[0].equals(anoUlt) && data[1].equals(mesUlt) && data[2].equals(diaUlt)) {
                                    veri = false;
                                    n++;
                                    ult_aux++;
                                }
                            }
                        }
                    }
                }

                if (n == 5) {

                    int X = Integer.parseInt(resolucao);
                    qtd = AnalisarSeries(data_horas, energia, posicao, energia_aux, datasHoras_aux, n, X, 0, opcao);
                    title = "AnalisarSeries_" + escolha;
                    Grafico(energia_aux, qtd, n, title);

                    Observacoes(energia_aux, qtd, dados, n);

                    int T = Integer.parseInt(tipoOrdenacao);
                    OrdenarMergeSort(energia_aux, qtd, n, T);
                    title = "OrdenarMergeSort_" + escolha;
                    Grafico(energia_aux, qtd, n, title);
                    qtd = AnalisarSeries(data_horas, energia, posicao, energia_aux, datasHoras_aux, n, X, 0, opcao);

                    int M = Integer.parseInt(modelo);
                    double nAlpha = Double.parseDouble(parModelo);
                    title = "EscolherMedia" + escolha;
                    qtd_aux = EscolherMedia(energia_aux, energia_media, qtd, n, M, nAlpha, title);
                    CalcularErro(energia_aux, energia_media, qtd_aux, dados, n);

                    Previsao(energia_aux, qtd, data_horas, posicao, datasHoras_aux, X, n, ult_aux, momentoPrevisao, M, dados, nAlpha);

                    ImprimirDados(dados);

                } else {
                    System.out.println("ERRO: Parâmetros inválidos!");
                }
            }

            if (s != 2 && s != 12) {
                System.out.println("ERRO: Número de Parâmetros errado!");
            }
        }
    }

    //1)---------------------------------------------------------------------------------------------------------------------------------------------------------
    public static int AnalisarSeries(String[][] data_horas, int[] energia, int posicao, int[] energia_aux, String[] datasHoras_aux, int n, int X, int aux, int opcao) {
        int extremo1, extremo2, qtd = 0;

        if (n != 5) {
            if (aux == 0) {
                opcao = tec.nextInt();
                System.out.println();
                System.out.println("============================================================================================================================");
                System.out.println();
            }
        } else {
            opcao = X;
        }

        while (qtd == 0) {
            switch (opcao) {
                case 11:
                    extremo1 = 6;
                    extremo2 = 11;
                    qtd = PartesDia(data_horas, energia, posicao, extremo1, extremo2, energia_aux, datasHoras_aux);
                    break;
                case 12:
                    extremo1 = 12;
                    extremo2 = 17;
                    qtd = PartesDia(data_horas, energia, posicao, extremo1, extremo2, energia_aux, datasHoras_aux);
                    break;
                case 13:
                    extremo1 = 18;
                    extremo2 = 23;
                    qtd = PartesDia(data_horas, energia, posicao, extremo1, extremo2, energia_aux, datasHoras_aux);
                    break;
                case 14:
                    extremo1 = 0;
                    extremo2 = 5;
                    qtd = PartesDia(data_horas, energia, posicao, extremo1, extremo2, energia_aux, datasHoras_aux);
                    break;
                case 2:
                    qtd = AnualMensalDiario(data_horas, energia, posicao, energia_aux, 2, datasHoras_aux);

                    break;
                case 3:
                    qtd = AnualMensalDiario(data_horas, energia, posicao, energia_aux, 1, datasHoras_aux);
                    break;
                case 4:
                    qtd = AnualMensalDiario(data_horas, energia, posicao, energia_aux, 0, datasHoras_aux);

                    break;
                default:
                    System.out.println();
                    System.out.println("============================================================================================================================");
                    System.out.println();
                    System.out.println("ERRO: Opção inválida!");
                    Submenu();
                    opcao = tec.nextInt();
                    break;
            }
        }
        return qtd;
    }

    //1)---------------------------------------------------------------------------------------------------------------------------------------------------------
    public static int PartesDia(String[][] data_horas, int[] energia, int posicao, int extremo1, int extremo2, int[] energia_aux, String[] datasHoras_aux) {

        int total = 0, qtd = 0, dia, i;

        String linha = data_horas[0][0];
        String[] info = linha.split("-");
        int diaComparacao = Integer.parseInt(info[2]);

        for (i = 0; i < posicao; i++) {

            String linhas = data_horas[i][0];
            String[] info1 = linhas.split("-");
            dia = Integer.parseInt(info1[2]);

            linhas = data_horas[i][1];
            String[] info2 = linhas.split(":");
            int horas = Integer.parseInt(info2[0]);

            if (dia != diaComparacao) {
                energia_aux[qtd] = total;
                datasHoras_aux[qtd] = data_horas[i - 1][0];
                diaComparacao = dia;

                if (horas >= extremo1 && horas <= extremo2) {
                    total = energia[i];
                } else {
                    total = 0;
                }

                qtd++;

            } else {
                if (horas >= extremo1 && horas <= extremo2) {
                    total = total + energia[i];
                }
            }
        }
        if (total != 0) {
            energia_aux[qtd] = total;
            datasHoras_aux[qtd] = data_horas[i - 1][0];
            return qtd + 1;
        } else {
            return qtd;
        }
    }

    //1)---------------------------------------------------------------------------------------------------------------------------------------------------------
    public static int AnualMensalDiario(String[][] data_horas, int[] energia, int posicao, int[] energia_aux, int coluna, String[] datasHoras_aux) {
        String amd, amd1;
        int total = 0, qtd = 0, i;

        String linha = data_horas[0][0];
        String[] info = linha.split("-");
        amd1 = info[coluna];

        for (i = 0; i < posicao; i++) {
            String linhas = data_horas[i][0];
            String[] info2 = linhas.split("-");
            amd = info2[coluna];
            if (amd.equals(amd1)) {
                total = energia[i] + total;
            } else {
                energia_aux[qtd] = total;
                datasHoras_aux[qtd] = data_horas[i - 1][0];
                qtd++;
                total = energia[i];
            }
            amd1 = amd;
        }
        energia_aux[qtd] = total;
        datasHoras_aux[qtd] = data_horas[i - 1][0];
        qtd++;

        return qtd;
    }

    //2)---------------------------------------------------------------------------------------------------------------------------------------------------------
    public static void Observacoes(int[] energia, int posicao, String[] dados, int n) throws FileNotFoundException {

        String dados_aux;
        double total = 0;
        int qtd1 = 0, qtd2 = 0, qtd3 = 0;

        for (int i = 0; i < posicao; i++) {
            total = energia[i] + total;
        }

        int media = (int) (total / posicao);

        for (int i = 0; i < posicao; i++) {
            if (energia[i] < (media - (0.2 * media))) {
                qtd1++;
            } else {
                if (energia[i] > (media - (0.2 * media)) && energia[i] < (media + (0.2 * media))) {
                    qtd2++;
                } else {
                    qtd3++;
                }
            }
        }

        GraficoHistograma(qtd1, qtd2, qtd3, n);

        if (n != 5) {
            System.out.println();
            System.out.println("Média global: " + media + " MW");
        } else {
            dados_aux = ("Média global: " + media + " MW");
            dados[0] = dados_aux;
        }

    }

    //3)---------------------------------------------------------------------------------------------------------------------------------------------------------
    public static void OrdenarMergeSort(int[] energia, int posicao, int n, int M) {
        boolean confirm = true;
        int opcao;

        if (n != 5) {
            opcao = tec.nextInt();

            System.out.println();
            System.out.println("============================================================================================================================");
            System.out.println();
        } else {
            opcao = M;
        }

        while (confirm) {
            switch (opcao) {
                case 1:
                    CrescenteMerge(energia, 0, posicao);
                    if (n != 5) {
                        System.out.println("Ordenação executada com sucesso!");
                    }
                    confirm = false;
                    break;

                case 2:
                    DecrescenteMerge(energia, 0, posicao);
                    if (n != 5) {
                        System.out.println("Ordenação executada com sucesso!");
                    }
                    confirm = false;
                    break;

                default:
                    System.out.println("erro: Opção Inválida!");
                    System.out.println();
                    System.out.print("Insira a opção que pretende: ");
                    opcao = tec.nextInt();
                    System.out.println();
                    break;
            }
        }
    }

    //3)---------------------------------------------------------------------------------------------------------------------------------------------------------
    public static void CrescenteMergeSort(int[] energia, int start, int middle, int end) {

        int[] temp = new int[end - start + 1];

        int i = start, j = middle + 1, k = 0;

        while (i <= middle && j <= end) {
            if (energia[i] <= energia[j]) {
                temp[k] = energia[i];
                k += 1;
                i += 1;
            } else {
                temp[k] = energia[j];
                k += 1;
                j += 1;
            }
        }

        while (i <= middle) {
            temp[k] = energia[i];
            k += 1;
            i += 1;
        }

        while (j <= end) {
            temp[k] = energia[j];
            k += 1;
            j += 1;
        }

        for (i = start; i <= end; i += 1) {
            energia[i] = temp[i - start];
        }

    }

    public static void CrescenteMerge(int[] energia, int start, int end) {
        if (start < end) {
            int mid = (start + end) / 2;
            CrescenteMerge(energia, start, mid);
            CrescenteMerge(energia, mid + 1, end);
            CrescenteMergeSort(energia, start, mid, end);
        }
    }

    //3)---------------------------------------------------------------------------------------------------------------------------------------------------------
    public static void DecrescenteMergeSort(int[] energia, int start, int middle, int end) {

        int[] temp = new int[end - start + 1];

        int i = start, j = middle + 1, k = 0;

        while (i <= middle && j <= end) {
            if (energia[i] >= energia[j]) {
                temp[k] = energia[i];
                k += 1;
                i += 1;
            } else {
                temp[k] = energia[j];
                k += 1;
                j += 1;
            }
        }

        while (i <= middle) {
            temp[k] = energia[i];
            k += 1;
            i += 1;
        }

        while (j <= end) {
            temp[k] = energia[j];
            k += 1;
            j += 1;
        }

        for (i = start; i <= end; i += 1) {
            energia[i] = temp[i - start];
        }

    }

    public static void DecrescenteMerge(int[] energia, int start, int end) {
        if (start < end) {
            int mid = (start + end) / 2;
            DecrescenteMerge(energia, start, mid);
            DecrescenteMerge(energia, mid + 1, end);
            DecrescenteMergeSort(energia, start, mid, end);
        }
    }

    //4)---------------------------------------------------------------------------------------------------------------------------------------------------------
    public static int EscolherMedia(int[] energia_aux, int[] energia_media, int qtd, int n_aux, int T, double nAlpha, String title) throws FileNotFoundException {
        int opcao, qtd2 = 0;
        boolean done = true;
        int n;
        double alpha;

        if (n_aux != 5) {
            opcao = tec.nextInt();

            System.out.println();
            System.out.println("============================================================================================================================");
            System.out.println();
        } else {
            opcao = T;
        }

        while (done) {
            switch (opcao) {
                case 1:
                    if (n_aux != 5) {
                        System.out.println("Insira a ordem da média móvel(n):");
                        System.out.print("n = ");

                        n = tec.nextInt();
                    } else {
                        n = (int) nAlpha;
                    }

                    qtd2 = CalcularMediaSimples(energia_aux, qtd, energia_media, n);
                    GraficoMediaSimples(energia_aux, qtd, energia_media, qtd2, n, "n", n_aux, title);
                    done = false;
                    break;

                case 2:
                    if (n_aux != 5) {
                        System.out.println("Insira a ordem da média móvel(α):");
                        System.out.print("α = ");

                        alpha = tec.nextDouble();
                    } else {
                        alpha = nAlpha;
                    }

                    CalcularMediaMovelPesada(energia_aux, qtd, energia_media, alpha);
                    qtd2 = qtd;
                    GraficoAlpha(energia_aux, qtd, energia_media, qtd2, alpha, "alpha", n_aux, title);
                    done = false;
                    break;

                default:
                    System.out.println("ERRO: Opção Inválida!");
                    System.out.println();
                    Submenu2();
                    opcao = tec.nextInt();
                    break;
            }
        }
        return qtd2;
    }

    //4)---------------------------------------------------------------------------------------------------------------------------------------------------------
    public static void CalcularErro(int[] energia, int[] energia_aux, int k, String[] dados, int n) {

        int erro_somatorio = 0;

        for (int i = 0; i < k; i++) {
            erro_somatorio = erro_somatorio + (Math.abs(energia_aux[i] - energia[i]));
        }

        if (n != 5) {
            System.out.println();
            System.out.println("MAE= " + (erro_somatorio / k) + " MW");
        } else {
            String dados_aux = "MAE= " + (erro_somatorio / k) + " MW";
            dados[1] = dados_aux;
        }
    }

    //4)---------------------------------------------------------------------------------------------------------------------------------------------------------
    public static int CalcularMediaSimples(int[] energia, int posicao, int[] energia_aux, int n) {

        while (n <= 0 || n > posicao) {
            System.out.println("ERRO: Valor errado: n ∈ ]0," + posicao + "]");
            System.out.println();
            System.out.print("n = ");
            n = tec.nextInt();
        }
        int total = 0, i;
        for (i = n - 1; i <= posicao; i++) {
            for (int j = i - n + 1; j <= i; j++) {
                total = energia[j] + total;
            }
            energia_aux[i] = (total / n);
            total = 0;
        }

        return i;
    }

    //4)---------------------------------------------------------------------------------------------------------------------------------------------------------
    public static void CalcularMediaMovelPesada(int[] energia, int posicao, int[] energia_aux, double alpha) {

        while (alpha <= 0 || alpha > 1) {
            System.out.println("ERRO: Valor errado: α ∈ ]0,1] ");
            System.out.println();
            System.out.print("α = ");

            alpha = tec.nextDouble();
        }
        energia_aux[0] = energia[0];

        for (int j = 1; j < posicao; j++) {
            energia_aux[j] = (int) ((alpha * energia[j]) + ((1 - alpha) * (energia_aux[j - 1])));
        }
    }

    //5)---------------------------------------------------------------------------------------------------------------------------------------------------------
    public static void Previsao(int[] energia_aux, int qtd, String[][] data_horas, int posicao, String[] datasHoras_aux, int opcao, int n, int ult_aux, String data_aux, int M, String[] dados, double nAlpha) {

        int local, ano, mes, comparacao;
        String ParteDoDia;

        String linha = data_horas[0][0];
        String[] info = linha.split("-");
        int anoMenor = Integer.parseInt(info[0]);

        String linha1 = data_horas[posicao - 1][0];
        String[] info1 = linha1.split("-");
        int anoMaior = Integer.parseInt(info1[0]);
        int ultimoMes = Integer.parseInt(info1[1]);

        switch (opcao) {
            case 11:
                ParteDoDia = "manhã";

                if (n != 5) {
                    comparacao = PrevisaoAux(qtd, datasHoras_aux, ParteDoDia, n, ult_aux);
                } else {
                    if (ult_aux == 1) {
                        comparacao = PrevisaoValidacoes2(datasHoras_aux, data_aux, qtd);
                    } else {
                        comparacao = qtd;
                    }
                }

                if (n != 5) {
                    opcao = PrevisaoSubmenu3();
                } else {
                    opcao = M;
                }

                MetodoPrevisao(energia_aux, opcao, comparacao, n, dados, nAlpha);
                break;

            case 12:
                ParteDoDia = "tarde";

                if (n != 5) {
                    comparacao = PrevisaoAux(qtd, datasHoras_aux, ParteDoDia, n, ult_aux);
                } else {
                    if (ult_aux == 1) {
                        comparacao = PrevisaoValidacoes2(datasHoras_aux, data_aux, qtd);
                    } else {
                        comparacao = qtd;
                    }
                }

                if (n != 5) {
                    opcao = PrevisaoSubmenu3();
                } else {
                    opcao = M;
                }

                MetodoPrevisao(energia_aux, opcao, comparacao, n, dados, nAlpha);
                break;

            case 13:
                ParteDoDia = "noite";

                if (n != 5) {
                    comparacao = PrevisaoAux(qtd, datasHoras_aux, ParteDoDia, n, ult_aux);
                } else {
                    if (ult_aux == 1) {
                        comparacao = PrevisaoValidacoes2(datasHoras_aux, data_aux, qtd);
                    } else {
                        comparacao = qtd;
                    }
                }

                if (n != 5) {
                    opcao = PrevisaoSubmenu3();
                } else {
                    opcao = M;
                }

                MetodoPrevisao(energia_aux, opcao, comparacao, n, dados, nAlpha);
                break;

            case 14:
                ParteDoDia = "madrugada";

                if (n != 5) {
                    comparacao = PrevisaoAux(qtd, datasHoras_aux, ParteDoDia, n, ult_aux);
                } else {
                    if (ult_aux == 1) {
                        comparacao = PrevisaoValidacoes2(datasHoras_aux, data_aux, qtd);
                    } else {
                        comparacao = qtd;
                    }
                }

                if (n != 5) {
                    opcao = PrevisaoSubmenu3();
                } else {
                    opcao = M;
                }

                MetodoPrevisao(energia_aux, opcao, comparacao, n, dados, nAlpha);
                break;

            case 2:
                ParteDoDia = "dia";

                if (n != 5) {
                    opcao = PrevisaoSubmenu4(qtd, datasHoras_aux, ParteDoDia);
                } else {
                    opcao = M;
                }

                if (opcao == 1) {
                    ParteDoDia = "";
                    if (n != 5) {
                        comparacao = PrevisaoValidacoes(datasHoras_aux, qtd, ParteDoDia);
                    } else {
                        if (ult_aux == 1) {
                            comparacao = PrevisaoValidacoes2(datasHoras_aux, data_aux, qtd);
                        } else {
                            comparacao = qtd;
                        }
                    }
                } else {
                    comparacao = qtd;
                }

                if (n != 5) {
                    opcao = PrevisaoSubmenu3();
                } else {
                    opcao = M;
                }

                MetodoPrevisao(energia_aux, opcao, comparacao, n, dados, nAlpha);
                break;
            case 3:
                ParteDoDia = "mes";

                if (n != 5) {
                    opcao = PrevisaoSubmenu4(qtd, datasHoras_aux, ParteDoDia);
                } else {
                    opcao = M;
                }

                if (opcao == 1) {
                    if (n != 5) {
                        System.out.print("Que mes quer prever (1-12) ? ");
                        mes = tec.nextInt();
                        while (mes < 1 || mes > 12) {
                            System.out.println("ERRO: mes Inválido!");
                            System.out.println();
                            System.out.print("Insira outro mes: ");
                            mes = tec.nextInt();
                        }

                        System.out.println();
                        System.out.print("De que ano quer prever o mes inserido? ");
                        ano = tec.nextInt();

                        while (ano < anoMenor || ano > anoMaior || (mes == 1 && ano == anoMenor) || (mes > ultimoMes && (ano == anoMaior))) {
                            System.out.println("ERRO: ERRO: A Série Temporal não contém o mes " + mes + " do ano " + ano + " !");
                            System.out.println();
                            System.out.print("De que ano quer prever o mes " + mes + " ? ");
                            ano = tec.nextInt();
                        }
                        System.out.println();
                    } else {
                        ano = Integer.parseInt(data_aux.substring(4, 8));
                        mes = Integer.parseInt(data_aux.substring(2, 4));
                    }

                    local = ((((ano - anoMenor) * 12) + mes) - 1);//"local" onde esta o mes escolhido

                } else {
                    local = qtd;
                }

                if (n != 5) {
                    opcao = PrevisaoSubmenu3();
                } else {
                    opcao = M;
                }

                MetodoPrevisao(energia_aux, opcao, local, n, dados, nAlpha);
                break;
            case 4:
                ParteDoDia = "ano";

                if (n != 5) {
                    opcao = PrevisaoSubmenu4(qtd, datasHoras_aux, ParteDoDia);
                } else {
                    opcao = M;
                }

                if (opcao == 1) {
                    if (n != 5) {
                        System.out.print("Que ano quer prever? ");
                        ano = tec.nextInt();

                        while (ano <= anoMenor || ano > anoMaior) {
                            System.out.println("ERRO: A Série Temporal não contém o ano " + ano + "!");
                            System.out.println();
                            System.out.print("Que ano quer prever? ");
                            ano = tec.nextInt();
                        }
                        System.out.println();
                    } else {
                        ano = Integer.parseInt(data_aux.substring(4, 8));
                    }

                    local = ano - anoMenor; //"local" onde esta o ano escolhido

                    if (n != 5) {
                        opcao = PrevisaoSubmenu3();
                    } else {
                        opcao = M;
                    }

                    MetodoPrevisao(energia_aux, opcao, local, n, dados, nAlpha);
                } else {
                    if (ultimoMes != 12) {
                        System.out.println("ERRO: O último ano da Série Temporal não se encontra completo!");
                    } else {
                        local = qtd;
                        if (n != 5) {
                            opcao = PrevisaoSubmenu3();
                        } else {
                            opcao = M;
                        }

                        MetodoPrevisao(energia_aux, opcao, local, n, dados, nAlpha);
                    }
                }
                break;

            default:
                System.out.println("ERRO: Opção inválida!");
                System.out.println();
                Submenu();
                break;
        }
    }

    //5)---------------------------------------------------------------------------------------------------------------------------------------------------------
    public static int PrevisaoValidacoes(String[] datasHoras_aux, int qtd, String ParteDoDia) {

        int ano, mes, dia, comparacao;

        if (ParteDoDia.equalsIgnoreCase("")) {
            System.out.print("Que dia (1-31) quer prever? ");
        } else {
            System.out.print("De que dia (1-31) quer prever a " + ParteDoDia + " ? ");
        }
        dia = tec.nextInt();
        while (dia < 1 || dia > 31) {
            System.out.println("ERRO: Dia Inválido!");
            System.out.println();
            System.out.print("Insira outro dia: ");
            dia = tec.nextInt();
        }
        System.out.println();
        System.out.print("De que mes (1-12) ? ");
        mes = tec.nextInt();
        while (mes < 1 || mes > 12) {
            System.out.println("ERRO: mes Inválido!");
            System.out.println();
            System.out.print("Insira outro mes: ");
            mes = tec.nextInt();
        }

        comparacao = -1;
        while (comparacao == -1) {
            for (int i = 0; i < qtd; i++) {
                String data = datasHoras_aux[i];
                String[] dataComparacao = data.split("-");
                int mesComparacao = Integer.parseInt(dataComparacao[1]);
                int diaComparacao = Integer.parseInt(dataComparacao[2]);
                if (mesComparacao == mes && diaComparacao == dia) {
                    comparacao = i;
                }
            }
            if (comparacao == -1) {
                System.out.println("ERRO: Não existe o dia que inseriu(" + dia + ") neste mes(" + mes + ") !");
                System.out.println();
                System.out.print("Insira outro mes: ");
                mes = tec.nextInt();
            }
        }

        System.out.println();
        System.out.print("De que ano? ");
        ano = tec.nextInt();
        comparacao = -1;
        while (comparacao == -1) {
            comparacao = getComparacao(datasHoras_aux, qtd, ano, mes, dia, comparacao);
            if (comparacao == -1) {
                if (ParteDoDia.equalsIgnoreCase("")) {
                    System.out.println("ERRO: A Série Temporal não contém esse dia!");
                    System.out.println();
                    System.out.print("De que ano quer prever o dia " + dia + " do mes " + mes + " ? ");
                    ano = tec.nextInt();
                } else {
                    System.out.println("ERRO: A Série Temporal não contém essa " + ParteDoDia + "!");
                    System.out.println();
                    System.out.print("De que ano quer prever a " + ParteDoDia + " do dia " + dia + " do mes " + mes + " ? ");
                    ano = tec.nextInt();
                }
            }
        }
        System.out.println();
        return comparacao;
    }

    //5)---------------------------------------------------------------------------------------------------------------------------------------------------------
    public static void MetodoPrevisao(int[] energia_aux, int opcao, int local, int n_aux, String[] dados, double nAlpha) {

        int total = 0, j, n;
        double alpha;
        int[] energia_aux2 = new int[20000];

        switch (opcao) {
            case 1:
                if (n_aux != 5) {
                    System.out.print("Insira a ordem(n): ");
                    n = tec.nextInt();
                    while (n < 1 || n > local) {
                        System.out.println("ERRO: Valor de n inválido: n ∈ [1," + local + "]");
                        System.out.println();
                        System.out.print("Insira a ordem(n): ");
                        n = tec.nextInt();
                    }
                    System.out.println();
                } else {
                    n = (int) nAlpha;
                }

                for (int i = local - n; i <= local - 1; i++) {
                    total = energia_aux[i] + total;
                }

                if (n_aux != 5) {
                    System.out.println("A previsao é de " + (total / n) + " MW");
                } else {
                    String dados_aux = ("A previsao é de " + (total / n) + " MW");
                    dados[2] = dados_aux;
                }

                break;

            case 2:
                if (n_aux != 5) {
                    System.out.print("Insira a constante(α): ");
                    alpha = tec.nextDouble();
                    while (alpha < 0 || alpha > 1) {
                        System.out.println("ERRO: Valor de α inválido: α ∈ ]0,1]");
                        System.out.println();
                        System.out.print("Insira a ordem(α): ");
                        alpha = tec.nextDouble();
                    }
                    System.out.println();
                } else {
                    alpha = nAlpha;
                }

                energia_aux2[0] = energia_aux[0];

                for (j = 1; j < local; j++) {
                    energia_aux2[j] = (int) ((alpha * energia_aux[j - 1]) + ((1 - alpha) * energia_aux2[j - 1]));
                }

                if (n_aux != 5) {
                    System.out.println("A previsao é de " + energia_aux2[j - 1] + " MW");
                } else {
                    String dados_aux = ("A previsao é de " + energia_aux2[j - 1] + " MW");
                    dados[2] = dados_aux;
                }
                break;
        }
    }

    //5)---------------------------------------------------------------------------------------------------------------------------------------------------------
    public static int PrevisaoAux(int qtd, String[] datasHoras_aux, String ParteDoDia, int n, int ult_aux) {

        int comparacao, opcao;

        if (n != 5) {
            Submenu4V2(qtd, datasHoras_aux, ParteDoDia);
            opcao = tec.nextInt();
        } else {
            opcao = ult_aux;
        }

        while (opcao != 1 && opcao != 2) {
            System.out.println();
            System.out.println("============================================================================================================================");
            System.out.println();
            System.out.println("ERRO: Opção Inválida!");
            Submenu4V2(qtd, datasHoras_aux, ParteDoDia);
            opcao = tec.nextInt();
        }
        System.out.println();
        System.out.println("============================================================================================================================");
        System.out.println();
        if (opcao == 1) {
            comparacao = PrevisaoValidacoes(datasHoras_aux, qtd, ParteDoDia);
        } else {
            comparacao = qtd;
        }
        return comparacao;
    }

    //5)---------------------------------------------------------------------------------------------------------------------------------------------------------
    public static int PrevisaoSubmenu3() {

        Submenu3();
        int opcao = tec.nextInt();
        while (opcao != 1 && opcao != 2) {
            System.out.println();
            System.out.println("============================================================================================================================");
            System.out.println();
            System.out.println("ERRO: Opção Inválida!");
            Submenu3();
            opcao = tec.nextInt();
        }
        System.out.println();
        System.out.println("============================================================================================================================");
        System.out.println();
        return opcao;
    }

    //5)---------------------------------------------------------------------------------------------------------------------------------------------------------
    public static int PrevisaoSubmenu4(int qtd, String[] datasHoras_aux, String ParteDoDia) {

        Submenu4(qtd, datasHoras_aux, ParteDoDia);
        int opcao = tec.nextInt();
        while (opcao != 1 && opcao != 2) {
            System.out.println();
            System.out.println("============================================================================================================================");
            System.out.println();
            System.out.println("ERRO: Opção Inválida!");
            Submenu4(qtd, datasHoras_aux, ParteDoDia);
            opcao = tec.nextInt();
        }
        System.out.println();
        System.out.println("============================================================================================================================");
        System.out.println();
        return opcao;
    }

    //6)---------------------------------------------------------------------------------------------------------------------------------------------------------
    public static int Novo(String[][] data_horas, int[] energia) throws FileNotFoundException {
        int posicao;

        String nome = tec.next();

        posicao = Leitura(data_horas, energia, nome);
        return posicao;
    }

    //TempoDeExecucao--------------------------------------------------------------------------------------------------------------------------------------------
    public static void TempoDeExecucao(int[] energia, int posicao) throws InterruptedException {

        long lStartTime = System.currentTimeMillis();

        calculation(energia, posicao);

        long lEndTime = System.currentTimeMillis();

        long output = lEndTime - lStartTime;

        System.out.println("Elapsed time in milliseconds: " + output);

    }

    //TempoDeExecucao--------------------------------------------------------------------------------------------------------------------------------------------
    private static void calculation(int[] energia, int posicao) throws InterruptedException {

        CrescenteMergeSort(energia, 0, (posicao - 1) / 2, posicao - 1);
        TimeUnit.SECONDS.sleep(2);

    }

    //================================(ORDENAMENTOS)================================
    //BubbleSort)------------------------------------------------------------------------------------------------------------------------------------------------
    public static void OrdenarBubbleSort(int[] energia_aux, int qtd, int n, String title) throws FileNotFoundException {
        int opcao = tec.nextInt();
        boolean done = true;
        System.out.println();
        System.out.println("============================================================================================================================");
        System.out.println();
        while (done) {
            switch (opcao) {
                case 1:
                    DecrescenteBubbleSort(energia_aux, qtd);
                    Grafico(energia_aux, qtd, n, title);
                    done = false;
                    break;

                case 2:
                    CrescenteBubbleSort(energia_aux, qtd);
                    Grafico(energia_aux, qtd, n, title);
                    done = false;
                    break;

                default:
                    System.out.println("ERRO: Opção Inválida!");
                    System.out.println();
                    Submenu2();
                    opcao = tec.nextInt();
                    break;
            }
        }
    }

    //BubbleSort)------------------------------------------------------------------------------------------------------------------------------------------------
    public static void CrescenteBubbleSort(int[] energia_aux, int posicao) {

        for (int idx1 = 0; idx1 < posicao; idx1++) {
            for (int idx2 = 0; idx2 < posicao; idx2++) {
                if (energia_aux[idx1] < energia_aux[idx2]) {

                    int auxiliar = energia_aux[idx2];
                    energia_aux[idx2] = energia_aux[idx1];
                    energia_aux[idx1] = auxiliar;
                }
            }
        }
    }

    //BubbleSort)------------------------------------------------------------------------------------------------------------------------------------------------
    public static void DecrescenteBubbleSort(int[] energia_aux, int posicao) {
        for (int idx1 = 0; idx1 < posicao; idx1++) {
            for (int idx2 = 0; idx2 < posicao; idx2++) {
                if (energia_aux[idx1] > energia_aux[idx2]) {

                    int auxiliar = energia_aux[idx2];
                    energia_aux[idx2] = energia_aux[idx1];
                    energia_aux[idx1] = auxiliar;

                }
            }
        }
    }

    //InsertionSort)---------------------------------------------------------------------------------------------------------------------------------------------
    public static void OrdenarInsertionSort(int[] energia_aux, int posicao, int n, String title) throws FileNotFoundException {

        int opcao = tec.nextInt();
        System.out.println();
        System.out.println("============================================================================================================================");
        System.out.println();
        while (true) {
            switch (opcao) {
                case 1:
                    CrescenteInsertionSort(energia_aux, posicao);
                    Grafico(energia_aux, posicao, n, title);
                    break;

                case 2:
                    DecrescenteInsertionSort(energia_aux, posicao);
                    Grafico(energia_aux, posicao, n, title);
                    break;

                default:
                    System.out.println("ERRO: Opção Inválida!");
                    break;
            }
        }
    }

    //InsertionSort)---------------------------------------------------------------------------------------------------------------------------------------------
    public static void CrescenteInsertionSort(int[] energia_aux, int posicao) {
        int i, aux, j;
        for (i = 1; i < posicao; i++) {
            aux = energia_aux[i];
            j = i - 1;

            while (j >= 0 && energia_aux[j] < aux) {
                energia_aux[j + 1] = energia_aux[j];
                j = j - 1;
            }
            energia_aux[j + 1] = aux;
        }
    }

    //InsertionSort)---------------------------------------------------------------------------------------------------------------------------------------------
    public static void DecrescenteInsertionSort(int[] energia_aux, int posicao) {
        int i, aux, j;
        for (i = 1; i < posicao; i++) {
            aux = energia_aux[i];
            j = i - 1;

            while (j >= 0 && energia_aux[j] > aux) {
                energia_aux[j + 1] = energia_aux[j];
                j = j - 1;
            }
            energia_aux[j + 1] = aux;
        }
    }

    //==============================================================================
    //===============================(GRÁFICOS)=====================================
    //Gráfico)---------------------------------------------------------------------------------------------------------------------------------------------------
    public static void Grafico(int[] energia_aux, int posicao, int n, String title) throws FileNotFoundException {
        int op;

        JavaPlot p = new JavaPlot();
        PlotStyle myPlotStyle = new PlotStyle();
        myPlotStyle.setStyle(Style.LINES);
        myPlotStyle.setLineWidth(1);
        myPlotStyle.setLineType(NamedPlotColor.BLUE);

        double[][] tab = new double[posicao][2];

        for (int i = 0; i < posicao; i++) {
            tab[i][0] = i;
            tab[i][1] = energia_aux[i];
        }

        DataSetPlot s = new DataSetPlot(tab);
        op = getOp(n, p, myPlotStyle, s);

        if (op == 1) {
            newFile(title, s);

            System.out.println("Ficheiro guardado em PNG.");
        }

        if (op == 2) {
            csvWriteGrafico(energia_aux, posicao, title);
            System.out.println("Ficheiro guardado em CSV.");
        }

        if (op == 3) {
            newFile(title, s);
            csvWriteGrafico(energia_aux, posicao, title);
            if (n != 5) {
                System.out.println("Ficheiros guardados em PNG e CSV.");
            }
        }

        if (op == 4) {
            System.out.println("Nenhum ficheiro guardado.");
        }

    }

    private static int getOp(int n, JavaPlot p, PlotStyle myPlotStyle, DataSetPlot s) {
        int op;
        s.setTitle("Consumo de Energia");
        s.setPlotStyle(myPlotStyle);

        if (n != 5) {
            p.addPlot(s);
            p.newGraph();
            p.plot();
        }

        if (n != 5) {
            System.out.println();
            System.out.println("============================================================================================================================");
            System.out.println();
            System.out.println("Pretende gravar o gráfico? 1.PNG 2.CSV 3.PNG e CSV 4.Não");
            System.out.println();
            System.out.print("Opção: ");
            op = tec.nextInt();

            while (op != 1 && op != 2 && op != 3 && op != 4) {
                System.out.println("ERRO: Opção Inválida!");
                System.out.println();
                System.out.println("Pretende gravar o gráfico? 1.PNG 2.CSV 3.PNG e CSV 4.Não");
                System.out.print("Opção: ");
                op = tec.nextInt();
            }
            System.out.println();
        } else {
            op = 3;
        }
        return op;
    }

    //GraficoHistograma)-----------------------------------------------------------------------------------------------------------------------------------------
    public static void GraficoHistograma(int qtd1, int qtd2, int qtd3, int n) throws FileNotFoundException {
        getDateTime();

        JavaPlot p = new JavaPlot();
        PlotStyle myPlotStyle = new PlotStyle();
        myPlotStyle.setStyle(Style.BOXES);
        myPlotStyle.setLineWidth(1);
        myPlotStyle.setLineType(NamedPlotColor.BLUE);
        myPlotStyle.setPointType(7);
        myPlotStyle.setPointSize(1);

        int[][] tab = new int[7][1];

        tab[1][0] = qtd1;
        tab[2][0] = qtd2;
        tab[3][0] = qtd3;

        DataSetPlot s = new DataSetPlot(tab);
        int op = getOp(n, p, myPlotStyle, s);

        if (op == 1) {

            String title = "Grafico de Barras";
            newFile(s, title);

            System.out.println("Ficheiro guardado em PNG.");
        }

        if (op == 2) {
            String title = "Histograma";
            csvWriteBarras(qtd1, qtd2, qtd3, title);
            System.out.println("Ficheiro guardado em CSV");
        }

        if (op == 3) {
            String title = "Histograma";
            newFile(s, title);
            csvWriteBarras(qtd1, qtd2, qtd3, title);
            if (n != 5) {
                System.out.println("Ficheiros guardados em PNG e CSV.");
            }
        }

        if (op == 4) {
            System.out.println("Nenhum ficheiro guardado.");
        }

    }

    private static void newFile(DataSetPlot s, String title) {
        new File("statistics_" + title + ".png");
        JavaPlot plot = new JavaPlot();
        GNUPlotTerminal terminal = new FileTerminal("png", "statistics_" + title + ".png");
        plot.setTerminal(terminal);
        plot.set("xlabel", "\"Observações\"");
        plot.set("ylabel", "\"" + title + "\"");
        plot.addPlot(s);
        PlotStyle stl = ((AbstractPlot) plot.getPlots().get(0)).getPlotStyle();
        stl.setStyle(Style.BOXES);
        plot.setKey(JavaPlot.Key.OFF);
        plot.plot();
    }

    //GraficoAlpha)----------------------------------------------------------------------------------------------------------------------------------------------
    public static void GraficoAlpha(int[] energia_aux, int qtd, int[] energia_aux2, int qtd3, double n, String caracter, int n_aux, String title) throws FileNotFoundException {
        JavaPlot p = new JavaPlot();
        PlotStyle myPlotStyle = new PlotStyle();
        myPlotStyle.setStyle(Style.LINES);
        myPlotStyle.setLineWidth(1);
        myPlotStyle.setLineType(NamedPlotColor.BLUE);

        PlotStyle myPlotStyle2 = new PlotStyle();
        myPlotStyle2.setStyle(Style.LINES);
        myPlotStyle2.setLineWidth(1);
        myPlotStyle2.setLineType(NamedPlotColor.MAGENTA);

        double[][] tab2 = new double[qtd3][2];
        double[][] tab = new double[qtd][2];

        for (int i = 0; i < qtd; i++) {
            tab2[i][0] = i;
            tab2[i][1] = energia_aux2[i];
        }
        for (int i = 0; i < qtd; i++) {
            tab[i][0] = i;
            tab[i][1] = energia_aux[i];
        }

        DataSetPlot s = new DataSetPlot(tab);
        DataSetPlot s2 = new DataSetPlot(tab2);
        s.setTitle("Atual");
        s.setPlotStyle(myPlotStyle);
        s2.setTitle(caracter + "=" + n);
        s2.setPlotStyle(myPlotStyle2);

        if (n_aux != 5) {
            p.addPlot(s);
            p.addPlot(s2);
            p.newGraph();
            p.plot();
        }

        int op;

        if (n != 5) {
            System.out.println();
            System.out.println("============================================================================================================================");
            System.out.println();
            System.out.println("Pretende gravar o gráfico? 1.PNG 2.CSV 3.PNG e CSV 4.Não");
            System.out.println();
            System.out.print("Opção: ");
            op = tec.nextInt();

            while (op != 1 && op != 2 && op != 3 && op != 4) {
                System.out.println("ERRO: Opção Inválida!");
                System.out.println();
                System.out.println("Pretende gravar o gráfico? 1.PNG 2.CSV 3.PNG e CSV 4.Não");
                System.out.print("Opção: ");
                op = tec.nextInt();
            }
            System.out.println();
        } else {
            op = 3;
        }

        if (op == 1) {

            title = "Consumo de energia";
            newFile(title, s);

            System.out.println("Ficheiro guardado em PNG.");
        }

        if (op == 2) {
            csvWriteGrafico(energia_aux, qtd, title);
            System.out.println("Ficheiro guardado em CSV.");
        }

        if (op == 3) {
            newFile(title, s);
            csvWriteMedias(energia_aux, energia_aux2, qtd, title);
            if (n_aux != 5) {
                System.out.println("Ficheiros guardados em PNG e CSV.");
            }
        }

        if (op == 4) {
            System.out.println("Nenhum ficheiro guardado.");
        }
    }

    private static void newFile(String title, DataSetPlot s) {
        new File("statistics_" + title + ".png");
        JavaPlot plot = new JavaPlot();
        GNUPlotTerminal terminal = new FileTerminal("png", "statistics_" + title + ".png");
        plot.setTerminal(terminal);
        plot.set("xlabel", "\"Observações\"");
        plot.set("ylabel", "\"" + title + "\"");
        plot.addPlot(s);
        PlotStyle stl = ((AbstractPlot) plot.getPlots().get(0)).getPlotStyle();
        stl.setStyle(Style.LINES);
        plot.setKey(JavaPlot.Key.OFF);
        plot.plot();
    }

    //GraficoMediaSimples)----------------------------------------------------------------------------------------------------------------------------------------------
    public static void GraficoMediaSimples(int[] energia_aux, int qtd, int[] energia_aux2, int qtd2, int n, String caracter, int n_aux, String title) throws FileNotFoundException {
        JavaPlot p = new JavaPlot();
        PlotStyle myPlotStyle = new PlotStyle();
        myPlotStyle.setStyle(Style.LINES);
        myPlotStyle.setLineWidth(1);
        myPlotStyle.setLineType(NamedPlotColor.BLUE);

        PlotStyle myPlotStyle2 = new PlotStyle();
        myPlotStyle2.setStyle(Style.LINES);
        myPlotStyle2.setLineWidth(1);
        myPlotStyle2.setLineType(NamedPlotColor.MAGENTA);

        double[][] tab2 = new double[qtd2 - n + 1][2];
        double[][] tab = new double[qtd][2];

        for (int i = n - 1; i < qtd2; i++) {
            tab2[i - n + 1][0] = i;
            tab2[i - n + 1][1] = energia_aux2[i];
        }
        for (int i = 0; i < qtd; i++) {
            tab[i][0] = i;
            tab[i][1] = energia_aux[i];
        }

        DataSetPlot s = new DataSetPlot(tab);
        DataSetPlot s2 = new DataSetPlot(tab2);
        s.setTitle("Atual");
        s.setPlotStyle(myPlotStyle);
        s2.setTitle(caracter + "=" + n);
        s2.setPlotStyle(myPlotStyle2);

        if (n_aux != 5) {
            p.addPlot(s);
            p.addPlot(s2);
            p.newGraph();
            p.plot();
        }

        int op;

        if (n != 5) {
            System.out.println();
            System.out.println("============================================================================================================================");
            System.out.println();
            System.out.println("Pretende gravar o gráfico? 1.PNG 2.CSV 3.PNG e CSV 4.Não");
            System.out.println();
            System.out.print("Opção: ");
            op = tec.nextInt();

            while (op != 1 && op != 2 && op != 3 && op != 4) {
                System.out.println("ERRO: Opção Inválida!");
                System.out.println();
                System.out.println("Pretende gravar o gráfico? 1.PNG 2.CSV 3.PNG e CSV 4.Não");
                System.out.print("Opção: ");
                op = tec.nextInt();
            }
            System.out.println();
        } else {
            op = 3;
        }

        if (op == 1) {

            newFile(title, s, s2);

            System.out.println("Ficheiro guardado em PNG.");
        }

        if (op == 2) {
            csvWriteMedias(energia_aux, energia_aux2, qtd, title);
            System.out.println("Ficheiro guardado em CSV.");
        }

        if (op == 3) {

            newFile(title, s, s2);
            csvWriteMedias(energia_aux, energia_aux2, qtd, title);
            if (n_aux != 5) {
                System.out.println("Ficheiros guardados em PNG e CSV.");
            }
        }

        if (op == 4) {
            System.out.println("Nenhum ficheiro guardado.");
        }
    }

    private static void newFile(String title, DataSetPlot s, DataSetPlot s2) {
        new File("statistics_" + title + ".png");

        JavaPlot plot = new JavaPlot();

        GNUPlotTerminal terminal = new FileTerminal("png", "statistics_" + title + ".png");
        plot.setTerminal(terminal);

        plot.set("xlabel", "\"Observações\"");
        plot.set("ylabel", "\"" + title + "\"");
        plot.addPlot(s);
        plot.addPlot(s2);
        PlotStyle stl = ((AbstractPlot) plot.getPlots().get(0)).getPlotStyle();
        stl.setStyle(Style.LINES);
        plot.setKey(JavaPlot.Key.OFF);
        plot.plot();
    }

    //getDateTime)----------------------------------------------------------------------------------------------------------------------------------------------
    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy  HH,mm,ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    //csvWriteMedias)----------------------------------------------------------------------------------------------------------------------------------------------

    public static void csvWriteMedias(int[] energia, int[] energia_aux, int posicao, String title) throws FileNotFoundException {
        String hora;
        hora = getDateTime();

        PrintWriter out = new PrintWriter(new File(title + ", " + hora + ".csv"));
        for (int i = 0; i < posicao; i++) {
            out.println(energia[i] + " (original)");
            out.printf("%d(filtrada)\n", energia_aux[i]);
        }

        out.close();
    }

    //csvWriteGrafico)----------------------------------------------------------------------------------------------------------------------------------------------
    public static void csvWriteGrafico(int[] energia, int size, String title) throws FileNotFoundException {
        String hora;
        hora = getDateTime();

        PrintWriter out = new PrintWriter(new File(title + ", " + hora + ".csv"));
        out.println("Consumption_MW");
        for (int i = 0; i < size; i++) {
            out.println(energia[i]);
        }

        out.close();
    }

    //csvWriteBarras)----------------------------------------------------------------------------------------------------------------------------------------------
    public static void csvWriteBarras(int qtd1, int qtd2, int qtd3, String title) throws FileNotFoundException {
        String hora;
        hora = getDateTime();

        PrintWriter out = new PrintWriter(new File(title + ", " + hora + ".csv"));
        out.println("Quantidade de valores dentro da média: " + qtd2);
        out.println("Quantidade de valores acima da média: " + qtd3);
        out.println("Quantidade de valores abaixo da média: " + qtd1);
        out.close();
    }

    //==============================================================================
    //===============================(SUBMENUS)=====================================
    //Submenu)---------------------------------------------------------------------------------------------------------------------------------------------------
    public static void Submenu() {

        System.out.println();
        System.out.println("==========================================================SUBMENU===========================================================");
        System.out.println();
        System.out.println("OPÇÃO 11:  Manhã (6:00 ás 11:59).");
        System.out.println("OPÇÃO 12:  Tarde (12:00 ás 17:59).");
        System.out.println("OPÇÃO 13:  Noite (18:00 ás 23:59).");
        System.out.println("OPÇÃO 14:  Madrugada (00:00 ás 05:59).");
        System.out.println("OPÇÃO  2:  Diário.");
        System.out.println("OPÇÃO  3:  Mensal.");
        System.out.println("OPÇÃO  4:  Anual.");
        System.out.println();
        System.out.print("Insira a opcao que pretende: ");
    }

    //Submenu2)--------------------------------------------------------------------------------------------------------------------------------------------------
    public static void Submenu2() {

        System.out.println();
        System.out.println("==========================================================SUBMENU===========================================================");
        System.out.println();
        System.out.println("OPÇÃO 1:  Ordenar de forma crescente.");
        System.out.println("OPÇÃO 2:  Ordenar de forma decrescente.");
        System.out.println();
        System.out.print("Insira a opção que pretende: ");
    }

    //Submenu3)--------------------------------------------------------------------------------------------------------------------------------------------------
    public static void Submenu3() {

        System.out.println();
        System.out.println("==========================================================SUBMENU===========================================================");
        System.out.println();
        System.out.println("OPÇÃO 1:  Média Móvel Simples.");
        System.out.println("OPÇÃO 2:  Média Móvel Pesada.");
        System.out.println();
        System.out.print("Insira a opção que pretende: ");
    }

    //Submenu4)---------------------------------------------------------------------------------------------------------------------------------------------------
    public static void Submenu4(int qtd, String[] datasHoras_aux, String caracter) {

        System.out.println();
        System.out.println("==========================================================SUBMENU===========================================================");
        System.out.println();
        String Inicio = datasHoras_aux[0];
        String Fim = datasHoras_aux[qtd - 1];
        System.out.println("OPÇÃO 1:  Prever um " + caracter + " que exista na Série Temporal (" + Inicio + " até " + Fim + ")");
        System.out.println("OPÇÃO 2:  Prever o próximo " + caracter + " ao ultimo existente no ficheiro.");
        System.out.println("(NOTA: Não é possível prever o primeiro " + caracter + " da Série Temporal e só é possível prever até o " + caracter + " a seguir ao último (caso se encontre completo) da Série Temporal!)");
        System.out.println();
        System.out.print("Insira a opção que pretende: ");
    }

    //Submenu4V2)---------------------------------------------------------------------------------------------------------------------------------------------------
    public static void Submenu4V2(int qtd, String[] datasHoras_aux, String caracter) {

        System.out.println();
        System.out.println("==========================================================SUBMENU===========================================================");
        System.out.println();
        String Inicio = datasHoras_aux[0];
        String Fim = datasHoras_aux[qtd - 1];
        System.out.println("OPÇÃO 1:  Prever uma " + caracter + " que exista na Série Temporal (" + Inicio + " até " + Fim + ")");
        System.out.println("OPÇÃO 2:  Prever a próxima " + caracter + " relativa á ultima existente no ficheiro.");
        System.out.println("(NOTA: Não é possível prever a primeira " + caracter + " da Série Temporal e só é possível prever até á " + caracter + " a seguir da última da Série Temporal!)");
        System.out.println();
        System.out.print("Insira a opção que pretende: ");
    }
    //==============================================================================
    //Imprimir)---------------------------------------------------------------------------------------------------------------------------------------------------

    public static void ImprimirDados(String[] dados) throws FileNotFoundException {
        PrintWriter Dados = new PrintWriter(new File("Dados.txt"));

        for (String dado : dados) {
            Dados.printf("%s\n", dado);
        }

        Dados.close();
    }

    //leitura)---------------------------------------------------------------------------------------------------------------------------------------------------
    public static int Leitura(String[][] data_horas, int[] energia, String ficheiro) throws FileNotFoundException {
        int posicao = 0;

        Scanner texto_dados = new Scanner(new File(ficheiro));
        texto_dados.nextLine();

        while (texto_dados.hasNextLine()) {
            String linha = texto_dados.nextLine();

            String[] info = linha.split(" ");
            data_horas[posicao][0] = info[0];
            linha = info[1];

            info = linha.split(",");
            data_horas[posicao][1] = info[0];
            energia[posicao] = Integer.parseInt(info[1]);

            posicao++;
        }

        texto_dados.close();

        for (int i = 49999; i > posicao; i--) {
            energia[i] = energia[49999];
            data_horas[i] = data_horas[49999];
        }

        return posicao;
    }

    public static int PrevisaoValidacoes2(String[] datasHoras_aux, String data_aux, int qtd) {

        int ano = Integer.parseInt(data_aux.substring(4, 8)), mes = Integer.parseInt(data_aux.substring(2, 4)), dia = Integer.parseInt(data_aux.substring(0, 2)), comparacao = 0;

        comparacao = getComparacao(datasHoras_aux, qtd, ano, mes, dia, comparacao);
        return comparacao;
    }

    private static int getComparacao(String[] datasHoras_aux, int qtd, int ano, int mes, int dia, int comparacao) {
        for (int i = 0; i < qtd; i++) {
            String data = datasHoras_aux[i];
            String[] dataComparacao = data.split("-");
            int anoComparacao = Integer.parseInt(dataComparacao[0]);
            int mesComparacao = Integer.parseInt(dataComparacao[1]);
            int diaComparacao = Integer.parseInt(dataComparacao[2]);
            if (mesComparacao == mes && diaComparacao == dia && anoComparacao == ano) {
                comparacao = i;
            }
        }
        return comparacao;
    }
}
