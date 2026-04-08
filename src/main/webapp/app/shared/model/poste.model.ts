import { Niveau } from 'app/shared/model/enumerations/niveau.model';
import { IManager } from 'app/shared/model/manager.model';

export interface IPoste {
  id?: number;
  nom?: string;
  niveau?: keyof typeof Niveau;
  manager?: IManager | null;
}

export const defaultValue: Readonly<IPoste> = {};
