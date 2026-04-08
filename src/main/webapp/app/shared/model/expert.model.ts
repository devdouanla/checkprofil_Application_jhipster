import { ICompetence } from 'app/shared/model/competence.model';
import { IUser } from 'app/shared/model/user.model';

export interface IExpert {
  id?: number;
  user?: IUser;
  competenceses?: ICompetence[] | null;
}

export const defaultValue: Readonly<IExpert> = {};
