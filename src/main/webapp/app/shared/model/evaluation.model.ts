import dayjs from 'dayjs';

import { IEmploye } from 'app/shared/model/employe.model';

export interface IEvaluation {
  id?: number;
  dateDebut?: dayjs.Dayjs;
  dateFin?: dayjs.Dayjs | null;
  conforme?: boolean | null;
  employe?: IEmploye | null;
}

export const defaultValue: Readonly<IEvaluation> = {
  conforme: false,
};
