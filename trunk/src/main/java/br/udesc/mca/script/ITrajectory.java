package br.udesc.mca.script;

/**
 * "Interface" para tentar simplificar e unificar o modelo da execução das diversas linguagens (R, Python, ...)
 */
public class ITrajectory {
    int ano;
    int mes;
    int dia;
    int hora;
    int min;
    int seg;

    float lat;
    float lon;
    float precisao;
}
