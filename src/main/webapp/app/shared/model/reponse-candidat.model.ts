import dayjs from 'dayjs';

import { IQuestionAsk } from 'app/shared/model/question-ask.model';
import { ISessionTest } from 'app/shared/model/session-test.model';

export interface IReponseCandidat {
  id?: number;
  estCorrecte?: boolean;
  dateReponse?: dayjs.Dayjs;
  questionAsk?: IQuestionAsk | null;
  session?: ISessionTest | null;
}

export const defaultValue: Readonly<IReponseCandidat> = {
  estCorrecte: false,
};
