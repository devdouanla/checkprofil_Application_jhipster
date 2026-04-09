import { ICompetence } from 'app/shared/model/competence.model';
import { Difficulte } from 'app/shared/model/enumerations/difficulte.model';

export interface IQuestion {
  id?: number;
  enonce?: string;
  reponseTexte?: string;
  points?: number;
  explication?: string | null;
  difficulte?: keyof typeof Difficulte;
  competence?: ICompetence | null;
}

export const defaultValue: Readonly<IQuestion> = {};
