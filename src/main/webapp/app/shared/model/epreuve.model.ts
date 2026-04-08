import { ICompetence } from 'app/shared/model/competence.model';
import { Difficulte } from 'app/shared/model/enumerations/difficulte.model';

export interface IEpreuve {
  id?: number;
  titre?: string;
  enonce?: string;
  difficulte?: keyof typeof Difficulte;
  duree?: number;
  genereParIA?: boolean;
  nbInt?: number | null;
  publie?: boolean;
  competence?: ICompetence | null;
}

export const defaultValue: Readonly<IEpreuve> = {
  genereParIA: false,
  publie: false,
};
