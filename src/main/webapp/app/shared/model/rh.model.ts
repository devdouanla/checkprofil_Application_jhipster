import { IUser } from 'app/shared/model/user.model';

export interface IRH {
  id?: number;
  user?: IUser;
}

export const defaultValue: Readonly<IRH> = {};
