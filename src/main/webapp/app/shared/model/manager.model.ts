import { IUser } from 'app/shared/model/user.model';

export interface IManager {
  id?: number;
  user?: IUser;
}

export const defaultValue: Readonly<IManager> = {};
