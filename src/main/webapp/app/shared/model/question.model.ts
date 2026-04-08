import { IEpreuve } from 'app/shared/model/epreuve.model';

export interface IQuestion {
  id?: number;
  enonce?: string;
  reponseTexte?: string | null;
  points?: number;
  explication?: string | null;
  epreuve?: IEpreuve | null;
}

export const defaultValue: Readonly<IQuestion> = {};
