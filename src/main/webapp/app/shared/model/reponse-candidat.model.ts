import dayjs from 'dayjs';

import { IQuestion } from 'app/shared/model/question.model';
import { ISessionTest } from 'app/shared/model/session-test.model';

export interface IReponseCandidat {
  id?: number;
  estCorrecte?: boolean;
  dateReponse?: dayjs.Dayjs;
  question?: IQuestion | null;
  session?: ISessionTest | null;
}

export const defaultValue: Readonly<IReponseCandidat> = {
  estCorrecte: false,
};
