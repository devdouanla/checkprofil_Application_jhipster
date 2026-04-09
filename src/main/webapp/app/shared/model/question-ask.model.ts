import { IQuestion } from 'app/shared/model/question.model';
import { ISessionTest } from 'app/shared/model/session-test.model';

export interface IQuestionAsk {
  id?: number;
  ordre?: number | null;
  question?: IQuestion | null;
  session?: ISessionTest | null;
}

export const defaultValue: Readonly<IQuestionAsk> = {};
