package com.jalrakshak.model;

public class ModelMetrics {

    private Double mae;
    private Double rmse;
    private Double r2;
    private Double within5mmPct;

    public ModelMetrics() {
    }

    public Double getMae() { return mae; }
    public void setMae(Double mae) { this.mae = mae; }

    public Double getRmse() { return rmse; }
    public void setRmse(Double rmse) { this.rmse = rmse; }

    public Double getR2() { return r2; }
    public void setR2(Double r2) { this.r2 = r2; }

    public Double getWithin5mmPct() { return within5mmPct; }
    public void setWithin5mmPct(Double within5mmPct) { this.within5mmPct = within5mmPct; }
}
