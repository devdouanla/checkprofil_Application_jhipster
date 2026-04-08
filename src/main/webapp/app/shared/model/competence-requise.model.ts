import { ICompetence } from 'app/shared/model/competence.model';
import { IPoste } from 'app/shared/model/poste.model';

export interface ICompetenceRequise {
  id?: number;
  obligatoire?: boolean;
  competence?: ICompetence | null;
  poste?: IPoste | null;
}

export const defaultValue: Readonly<ICompetenceRequise> = {
  obligatoire: false,
};
