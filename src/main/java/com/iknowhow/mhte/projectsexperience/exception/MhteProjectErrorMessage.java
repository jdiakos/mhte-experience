package com.iknowhow.mhte.projectsexperience.exception;

public enum MhteProjectErrorMessage {
	PROJECT_NOT_FOUND,
	PROJECT_CONTRACTOR_NOT_FOUND,
	PROJECT_SUBCONTRACTOR_NOT_FOUND,
	CONSTRAINT_VALIDATION_ERROR,
	PUBLIC_PROJECTS_MUST_HAVE_AN_ADAM,
	NON_PUBLIC_PROJECTS_CANNOT_HAVE_AN_ADAM,
	// @TODO - GENERIFY MORE THIS MESSAGE
	DATABASE_DATA_INTEGRITY_VIOLATION,
	ALREADY_ASSIGNED,
	TOTAL_PERCENTAGE_EXCEEDS_MAX,
	CONTRACT_NOT_FOUND,
	ADAM_ALREADY_EXISTS,
	PROTOCOL_NUMBER_ALREADY_EXISTS,
	VALUES_CANNOT_BE_NEGATIVE,
	FILE_CANNOT_BE_UPLOADED,
	FILENET_FOLDER_ERROR,
	FILE_TYPE_NOT_ALLOWED,
	CONTRACT_ALREADY_ASSIGNED,
	NO_INITIAL_CONTRACT_PRESENT,
	MORE_THAN_ONE_INITIAL_CONTRACTS_PRESENT,
	CONTRACTOR_ALREADY_ASSIGNED,
	SUBCONTRACTOR_ALREADY_ASSIGNED
}
