package ca.bc.gov.open.jag.api.model.data;

import java.util.Date;
import java.util.List;

public class ClientProfile {
    private Client client;
    private String offense;
    private String nextCourtDate;
    private String secondaryManager;
    private String status;
    private String supervisionLevel;
    private String orders;
    private String finalOrderExpiryDt;
    private Date orderEffectiveDt;
    private String releaseReason;
    private String institutionstatus;
    private String clientinoutyn;
    private String clientType;
    private String internalLocation;
    private String outLocation;
    private String outReason;
    private String iaStatus;
    private String popDesignation;
    private String icayraSecurity;
    private String icayraSecurityStatus;
    private String federalParolOffice;
    private String iaClassification;
    private String custody;
    private String possibleParoleDt;
    private String probableDischargeDt;
    private String finalWarrantExpiryDt;
    private List<Warrant> warrants;
    private List<Alert> alerts;
    private List<Program> programs;
    private String isBiometricEnrolled;
    private String eServicesStatus;
    private String biometricStatus;
    private String eReporting;

    public String getPossibleParoleDt() {
        return possibleParoleDt;
    }

    public void setPossibleParoleDt(String possibleParoleDt) {
        this.possibleParoleDt = possibleParoleDt;
    }

    public String getProbableDischargeDt() {
        return probableDischargeDt;
    }

    public void setProbableDischargeDt(String probableDischargeDt) {
        this.probableDischargeDt = probableDischargeDt;
    }

    public String getFinalWarrantExpiryDt() {
        return finalWarrantExpiryDt;
    }

    public void setFinalWarrantExpiryDt(String finalWarrantExpiryDt) {
        this.finalWarrantExpiryDt = finalWarrantExpiryDt;
    }

    public String getSecondaryManager() {
        return secondaryManager;
    }

    public void setSecondaryManager(String secondaryManager) {
        this.secondaryManager = secondaryManager;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSupervisionLevel() {
        return supervisionLevel;
    }

    public void setSupervisionLevel(String supervisionLevel) {
        this.supervisionLevel = supervisionLevel;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public String getFinalOrderExpiryDt() {
        return finalOrderExpiryDt;
    }

    public void setFinalOrderExpiryDt(String finalOrderExpiryDt) {
        this.finalOrderExpiryDt = finalOrderExpiryDt;
    }

    public String getReleaseReason() {
        return releaseReason;
    }

    public void setReleaseReason(String releaseReason) {
        this.releaseReason = releaseReason;
    }

    public String getInstitutionstatus() {
        return institutionstatus;
    }

    public void setInstitutionstatus(String institutionstatus) {
        this.institutionstatus = institutionstatus;
    }

    public String getClientinoutyn() {
        return clientinoutyn;
    }

    public void setClientinoutyn(String clientinoutyn) {
        this.clientinoutyn = clientinoutyn;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getInternalLocation() {
        return internalLocation;
    }

    public void setInternalLocation(String internalLocation) {
        this.internalLocation = internalLocation;
    }

    public String getOutLocation() {
        return outLocation;
    }

    public void setOutLocation(String outLocation) {
        this.outLocation = outLocation;
    }

    public String getOutReason() {
        return outReason;
    }

    public void setOutReason(String outReason) {
        this.outReason = outReason;
    }

    public String getIaStatus() {
        return iaStatus;
    }

    public void setIaStatus(String iaStatus) {
        this.iaStatus = iaStatus;
    }

    public String getPopDesignation() {
        return popDesignation;
    }

    public void setPopDesignation(String popDesignation) {
        this.popDesignation = popDesignation;
    }

    public String getIcayraSecurity() {
        return icayraSecurity;
    }

    public void setIcayraSecurity(String icayraSecurity) {
        this.icayraSecurity = icayraSecurity;
    }

    public String getIcayraSecurityStatus() {
        return icayraSecurityStatus;
    }

    public void setIcayraSecurityStatus(String icayraSecurityStatus) {
        this.icayraSecurityStatus = icayraSecurityStatus;
    }

    public List<Warrant> getWarrants() {
        return warrants;
    }

    public void setWarrants(List<Warrant> warrants) {
        this.warrants = warrants;
    }

    public String getFederalParolOffice() {
        return federalParolOffice;
    }

    public void setFederalParolOffice(String federalParolOffice) {
        this.federalParolOffice = federalParolOffice;
    }

    public String getIsBiometricEnrolled() {
        return isBiometricEnrolled;
    }

    public void setIsBiometricEnrolled(String isBiometricEnrolled) {
        this.isBiometricEnrolled = isBiometricEnrolled;
    }

    public String geteServicesStatus() {
        return eServicesStatus;
    }

    public void seteServicesStatus(String eServicesStatus) {
        this.eServicesStatus = eServicesStatus;
    }

    public String getBiometricStatus() {
        return biometricStatus;
    }

    public void setBiometricStatus(String biometricStatus) {
        this.biometricStatus = biometricStatus;
    }

    public String geteReporting() {
        return eReporting;
    }

    public void seteReporting(String eReporting) {
        this.eReporting = eReporting;
    }

    public String getOffense() {
        return offense;
    }

    public void setOffense(String offense) {
        this.offense = offense;
    }

    public String getNextCourtDate() {
        return nextCourtDate;
    }

    public void setNextCourtDate(String nextCourtDate) {
        this.nextCourtDate = nextCourtDate;
    }

    public String getIaClassification() {
        return iaClassification;
    }

    public void setIaClassification(String iaClassification) {
        this.iaClassification = iaClassification;
    }

    public String getCustody() {
        return custody;
    }

    public void setCustody(String custody) {
        this.custody = custody;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }

    public List<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }

    public Date getOrderEffectiveDt() {
        return orderEffectiveDt;
    }

    public void setOrderEffectiveDt(Date orderEffectiveDt) {
        this.orderEffectiveDt = orderEffectiveDt;
    }

}
