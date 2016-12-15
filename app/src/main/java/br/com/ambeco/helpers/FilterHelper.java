package br.com.ambeco.helpers;

/**
 * Created by Ambeco on 14/12/16.
 */

public class FilterHelper {

    private boolean filterDeslizamento;
    private boolean FilterDesmatamento;
    private boolean filterQueimada;
    private boolean filterLixo;

    public boolean isFilterDeslizamento() {
        return filterDeslizamento;
    }

    public void setFilterDeslizamento(boolean filterDeslizamento) {
        this.filterDeslizamento = filterDeslizamento;
    }

    public boolean isFilterDesmatamento() {
        return FilterDesmatamento;
    }

    public void setFilterDesmatamento(boolean filterDesmatamento) {
        FilterDesmatamento = filterDesmatamento;
    }

    public boolean isFilterQueimada() {
        return filterQueimada;
    }

    public void setFilterQueimada(boolean filterQueimada) {
        this.filterQueimada = filterQueimada;
    }

    public boolean isFilterLixo() {
        return filterLixo;
    }

    public void setFilterLixo(boolean filterLixo) {
        this.filterLixo = filterLixo;
    }
}
