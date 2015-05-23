package br.udesc.mca.trajectory.service;

/**
 * Servico para inserir trajetorias das mais diversas formas nos diferentes
 * modos de persistencia
 */
public interface TrajectoryInput {

    /**
     * Inclui um ponto em uma nova trajetoria e retorna o ID da trajetoria
     * criada
     */
    public long insertSinglePointWithDateTime(float latitude, float longitude, int ano, int mes, int dia, int hora,
            int min, int seg, String descricao);

    /**
     * Iclui um novo ponto em uma trajetoria usando a data/hora atual. Para uma
     * nova trajetoria informar um id < 1 para <code>idTrajetoria</code>
     */
    public long insertSinglePoint(long idTrajetoria, float latitude, float longitude);
}
