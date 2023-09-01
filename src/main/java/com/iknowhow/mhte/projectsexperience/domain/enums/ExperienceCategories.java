package com.iknowhow.mhte.projectsexperience.domain.enums;

public enum ExperienceCategories {

    INDUSTRIAL ("Industrial studies"),
    TOPOGRAPHICAL ("Topographical studies"),
    CHEMICAL ("Chemical studies"),
    CHEMICAL_ENGINEERING_AND_INSTALLATION ("Chemical engineering and chemical installation studies"),
    METALLURGICAL ("Metallurgical studies"),
    GEOLOGICAL_HYDROGEOLOGICAL_GEOPHYSICAL ("Geological, hydrogeological, and geophysical studies"),
    GEOTECHNICAL ("Geotechnical studies"),
    EDAPHOLOGICAL ("Edaphological studies"),
    AGRICULTURAL ("Agricultural studies"),
    FORESTRY ("Forestry studies"),
    GREEN_PROJECTS ("Green projects and green configuration of surrounding space"),
    FISHERIES ("Fisheries studies"),
    ENVIRONMENTAL ("Environmental studies"),
    COMPUTER_NETWORK ("Computer systems and networks studies"),
    SPATIAL_AND_REGULATORY_STUDIES ("Spatial And Regulatory Studies"),
    URBAN_AND_SPATIAL_STUDIES ("Urban And Spatial Studies"),
    FINANCIAL_STUDIES("Financial Studies"),
    SOCIAL_STUDIES ("Social Studies"),
    ORGANIZATIONAL_AND_BUSINESS_RESEARCH_STUDIES ("Organizational And Business Research Studies"),
    ARCHITECTURAL_STUDIES_OF_BUILDING_PROJECTS ("Architectural Studies Of Building Projects"),
    SPECIAL_ARCHITECTURAL_STUDIES ("Special Archtectural Studies"),
    STATIC_STUDIES ("Static Studies"),
    MECHANICAL_ELECTRICAL_ELECTRIC_STUDIES ("Mechanical, Electrical, Electric Studies"),
    TRANSPORTATION_AND_TRAFFIC_STUDIES ("Transportation And Traffic Studies"),
    HARBOR_WORKS_STUDIES ("Harbor Projects Studies"),
    STUDIES_OF_MEANS_OF_TRANSPORT ("Studies Of Means Of Transport"),
    STUDIES_OF_HYDRAULIC_PROJECTS ("Studies Of Hydraulic Projects And Water Resources Management"),
    ENERGY_STUDIES ("Energy Studies"),
    ROAD_CONSTRUCTION ("Road Construction"),
    CONSTRUCTION ("Construction"),
    HYDRAULIC ("Hydraulic"),
    ELECTROMECHANICAL ("Electromechanical"),
    PORT ("Port"),
    INDUSTRIAL_ENERGY ("Industrial_Energy")
    ;

    final String label;

    ExperienceCategories(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
