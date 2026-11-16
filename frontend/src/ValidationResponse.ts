export const ValidationStatus = {
    VALID: "VALID",
    INVALID: "INVALID",
    INCOMPLETE: "INCOMPLETE"
} as const;

export type ValidationStatus = (typeof ValidationStatus)[keyof typeof ValidationStatus];

export interface ValidationResponse {
    status: ValidationStatus,
    message: string,
    bankName: string
}