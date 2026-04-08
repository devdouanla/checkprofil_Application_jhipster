import dayjs from 'dayjs';

import { ApiResult, apiGet, apiPost, apiPut } from './api-store';
import { getManagerworkApiEndpoint } from '../managerwork.config';

import { ISessionTest } from 'app/shared/model/session-test.model';

const BASE = getManagerworkApiEndpoint('sessionTests');

export const fetchManagerworkSessionTestsByEvaluation = (evaluationId: number, signal?: AbortSignal): Promise<ApiResult<ISessionTest[]>> =>
  apiGet<ISessionTest[], { 'evaluationId.equals': number; size: number; sort: string }>(BASE, {
    params: { 'evaluationId.equals': evaluationId, size: 200, sort: 'id,asc' },
    signal,
  });

export const createManagerworkSessionTest = (
  evaluationId: number,
  epreuveId: number,
  signal?: AbortSignal,
): Promise<ApiResult<ISessionTest>> =>
  apiPost<ISessionTest, Partial<ISessionTest>>(
    BASE,
    {
      evaluation: { id: evaluationId },
      epreuves: { id: epreuveId },
      dateDebut: dayjs(),
    },
    { signal },
  );

export const updateManagerworkSessionTest = (sessionTest: ISessionTest, signal?: AbortSignal): Promise<ApiResult<ISessionTest>> =>
  apiPut<ISessionTest, ISessionTest>(`${BASE}/${sessionTest.id}`, sessionTest, { signal });
