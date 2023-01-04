package com.example.partymanager_inzprog;

import java.time.LocalDate;

public class PartyData {
    private Integer party_id;
    private String partyName;
    private String partyAddress;
    private LocalDate partyOpeningDate;
    private LocalDate partyClosingDate;
    private Double priceOfTheParty;
    private Integer availableEntries;

    public PartyData(Integer party_id, String partyName, String partyAddress, LocalDate partyOpeningDate, LocalDate partyClosingDate, Double priceOfTheParty, Integer availableEntries) {
        this.party_id = party_id;
        this.partyName = partyName;
        this.partyAddress = partyAddress;
        this.partyOpeningDate = partyOpeningDate;
        this.partyClosingDate = partyClosingDate;
        this.priceOfTheParty = priceOfTheParty;
        this.availableEntries = availableEntries;
    }

    public Integer getParty_id() {
        return party_id;
    }
    public String getPartyName() {
        return partyName;
    }
    public String getPartyAddress() {
        return partyAddress;
    }
    public LocalDate getPartyOpeningDate() {
        return partyOpeningDate;
    }
    public LocalDate getPartyClosingDate() {
        return partyClosingDate;
    }
    public Double getPriceOfTheParty() {
        return priceOfTheParty;
    }
    public Integer getAvailableEntries() {
        return availableEntries;
    }
}
