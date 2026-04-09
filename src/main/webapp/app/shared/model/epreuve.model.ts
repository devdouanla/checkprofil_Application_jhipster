import { ICompetence } from 'app/shared/model/competence.model';
import { Difficulte } from 'app/shared/model/enumerations/difficulte.model';

export interface IEpreuve {
  id?: number;
  titre?: string;
  enonce?: string;
  difficulte?: keyof typeof Difficulte;
  duree?: number;
  nbQuestions?: number;
  genereParIA?: boolean;
  publie?: boolean;
  competence?: ICompetence;
}

export const defaultValue: Readonly<IEpreuve> = {
  genereParIA: false,
  publie: false,
};
