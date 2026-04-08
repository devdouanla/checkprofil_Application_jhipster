import dayjs from 'dayjs';

import { IEpreuve } from 'app/shared/model/epreuve.model';
import { IEvaluation } from 'app/shared/model/evaluation.model';
import { IResultat } from 'app/shared/model/resultat.model';

export interface ISessionTest {
  id?: number;
  scoreObtenu?: number | null;
  dateDebut?: dayjs.Dayjs;
  resultat?: IResultat | null;
  evaluation?: IEvaluation | null;
  epreuves?: IEpreuve | null;
}

export const defaultValue: Readonly<ISessionTest> = {};
