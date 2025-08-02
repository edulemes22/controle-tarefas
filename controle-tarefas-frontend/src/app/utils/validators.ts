import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import moment from 'moment';

export function dateNotInPast(control: AbstractControl): ValidationErrors | null {
  if (!control.value) return null;

  const selectedDate = moment(control.value);
  const today = moment().startOf('day');

  return selectedDate.isBefore(today) ? { dateInPast: true } : null;
}
