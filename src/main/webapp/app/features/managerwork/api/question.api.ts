import { ApiResult, apiGet } from './api-store';
import { getManagerworkApiEndpoint } from '../managerwork.config';

import { IQuestion } from 'app/shared/model/question.model';

const BASE = getManagerworkApiEndpoint('questions');

export const fetchManagerworkQuestionsByEpreuve = (epreuveId: number, signal?: AbortSignal): Promise<ApiResult<IQuestion[]>> =>
  apiGet<IQuestion[]>(`${BASE}/epreuve/${epreuveId}`, { signal });
