import * as React from 'react'
import {useEffect, useState} from 'react'
import './App.css'
import {Alert, Button, Snackbar, Stack, TextField} from "@mui/material";
import type {ValidationResponse, ValidationStatus} from "./ValidationResponse.ts";
import {ValidationStatus as ValidationStatusValue} from "./ValidationResponse.ts";

function App() {
  const [iban, setIban] = useState("")
  const [validating, setValidating] = useState(false)
  const [validationStatus, setValidationStatus] = useState<ValidationStatus>(ValidationStatusValue.INCOMPLETE)
  const [bankName, setBankName] = useState("")
  const [validationError, setValidationError] = useState("")
  const [saving, setSaving] = useState(false)
  const [openSavedSnackbar, setOpenSavedSnackbar] = useState(false);
  const [openErrorSnackbar, setOpenErrorSnackbar] = useState(false);

  const validate = async () => {
    setValidating(true)
    setValidationStatus(ValidationStatusValue.INCOMPLETE)
    setValidationError("")
    try {
      const filteredIban = iban.replace(/[^a-zA-Z0-9]/g, "").toUpperCase();
      const response = await fetch(`/iban/validate?value=${filteredIban}`)
      const validation: ValidationResponse = await response.json()
      setValidationStatus(validation.status)
      setBankName(validation.bankName)
      if (validation.status === ValidationStatusValue.INVALID) {
        setValidationError('Die IBAN ist nicht korrekt.')
      }
    } catch {
      setValidationStatus(ValidationStatusValue.INVALID)
      setValidationError('Die Iban konnte nicht validiert werden.')
    } finally {
      setValidating(false)
    }
  }

  useEffect(() => {
    validate()
  }, [iban])

  async function onIbanChange(e: React.ChangeEvent<HTMLInputElement>) {
    setValidationStatus(ValidationStatusValue.INCOMPLETE)
    setValidationError("")
    setIban(e.target.value);
  }

  async function saveIban() {
    try {
      setSaving(true)
      const response = await fetch('/iban', {
        method: 'POST',
        body: iban,
      })
      if (response.ok) {
        setIban("")
        setOpenSavedSnackbar(true)
      } else {
        setOpenErrorSnackbar(true)
      }
    } catch {
      setOpenErrorSnackbar(true)
    } finally {
      setSaving(false)
    }
  }

  const helperText = validationStatus === ValidationStatusValue.VALID ? `Bank: ${bankName}` : validationError

  return (
    <>
      <div id="center">
        <Stack sx={{maxWidth: 800, minWidth: 400, gap: 2}}>
          <p>IBAN Validator</p>
          <TextField value={iban} onChange={onIbanChange} size="medium"
            error={!!validationError}
            helperText={helperText} />
          <Button
              loading={saving}
              disabled={validationStatus !== ValidationStatusValue.VALID || validating}
              onClick={() => {saveIban()}}
              variant="contained">
            Speichern
          </Button>
        </Stack>
      </div>
      <Snackbar
          open={openSavedSnackbar}
          autoHideDuration={6000}
          onClose={() => setOpenSavedSnackbar(false)}>
          <Alert
            onClose={ () => setOpenSavedSnackbar(false)}
            severity="success"
            variant="filled"
            sx={{ width: '100%' }}
            >
          Iban wurde gespeichert.
        </Alert>
      </Snackbar>
      <Snackbar
          open={openErrorSnackbar}
          autoHideDuration={6000}
          onClose={() => setOpenErrorSnackbar(false)}>
          <Alert
            onClose={ () => setOpenErrorSnackbar(false)}
            severity="error"
            variant="filled"
            sx={{ width: '100%' }}
            >
          Es gab einen Fehler beim Speichern.
        </Alert>
      </Snackbar>
    </>
  )
}

export default App
