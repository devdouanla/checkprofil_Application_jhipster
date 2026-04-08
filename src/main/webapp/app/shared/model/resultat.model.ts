import { Mention } from 'app/shared/model/enumerations/mention.model';

export interface IResultat {
  id?: number;
  scoreTotal?: number;
  scoreMax?: number;
  pourcentage?: number;
  mention?: keyof typeof Mention;
}

export const defaultValue: Readonly<IResultat> = {};
