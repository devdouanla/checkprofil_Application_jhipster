import { IExpert } from 'app/shared/model/expert.model';

export interface ICompetence {
  id?: number;
  nom?: string;
  expertses?: IExpert[] | null;
}

export const defaultValue: Readonly<ICompetence> = {};
