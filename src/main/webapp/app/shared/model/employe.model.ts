import dayjs from 'dayjs';

import { IPoste } from 'app/shared/model/poste.model';

export interface IEmploye {
  id?: number;
  nom?: string;
  dateRecrutement?: dayjs.Dayjs;
  poste?: IPoste | null;
}

export const defaultValue: Readonly<IEmploye> = {};
