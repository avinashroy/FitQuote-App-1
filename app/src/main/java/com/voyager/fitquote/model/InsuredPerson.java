package com.voyager.fitquote.model;

public class InsuredPerson {
    private String firstName;

    private String preferredPlan;

    private String involvedPartyIdentificationNumber;

    private String relationshipWithApplicant;

    private String age;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPreferredPlan() {
        return preferredPlan;
    }

    public void setPreferredPlan(String preferredPlan) {
        this.preferredPlan = preferredPlan;
    }

    public String getInvolvedPartyIdentificationNumber() {
        return involvedPartyIdentificationNumber;
    }

    public void setInvolvedPartyIdentificationNumber(String involvedPartyIdentificationNumber) {
        this.involvedPartyIdentificationNumber = involvedPartyIdentificationNumber;
    }

    public String getRelationshipWithApplicant() {
        return relationshipWithApplicant;
    }

    public void setRelationshipWithApplicant(String relationshipWithApplicant) {
        this.relationshipWithApplicant = relationshipWithApplicant;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "InsuredPerson{" +
                "firstName='" + firstName + '\'' +
                ", preferredPlan='" + preferredPlan + '\'' +
                ", involvedPartyIdentificationNumber='" + involvedPartyIdentificationNumber + '\'' +
                ", relationshipWithApplicant='" + relationshipWithApplicant + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
