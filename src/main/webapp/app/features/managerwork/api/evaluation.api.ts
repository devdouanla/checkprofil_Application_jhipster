import dayjs from 'dayjs';

import { ApiResult, apiGet, apiPost, apiPut } from './api-store';
import { getManagerworkApiEndpoint } from '../managerwork.config';

import { IEvaluation } from 'app/shared/model/evaluation.model';
import { IManager } from 'app/shared/model/manager.model';

const EVALUATION_BASE = getManagerworkApiEndpoint('evaluations');
const MANAGER_BASE = getManagerworkApiEndpoint('managers');

export const fetchManagerworkManagerByUserId = (userId: number, signal?: AbortSignal): Promise<ApiResult<IManager[]>> =>
  apiGet<IManager[], { 'userId.equals': number; size: number }>(MANAGER_BASE, {
    params: { 'userId.equals': userId, size: 1 },
    signal,
  });

export const fetchManagerworkEvaluationsByManager = (managerId: number, signal?: AbortSignal): Promise<ApiResult<IEvaluation[]>> =>
  apiGet<IEvaluation[], { 'employePosteManagerId.equals': number; size: number; sort: string }>(EVALUATION_BASE, {
    params: { 'employePosteManagerId.equals': managerId, size: 200, sort: 'dateDebut,desc' },
    signal,
  });

export const fetchManagerworkEvaluationsEnCoursByManager = (managerId: number, signal?: AbortSignal): Promise<ApiResult<IEvaluation[]>> =>
  apiGet<IEvaluation[], { 'employePosteManagerId.equals': number; 'dateFin.specified': boolean; size: number; sort: string }>(
    EVALUATION_BASE,
    {
      params: { 'employePosteManagerId.equals': managerId, 'dateFin.specified': false, size: 200, sort: 'dateDebut,desc' },
      signal,
    },
  );

export const fetchManagerworkEvaluation = (evaluationId: number, signal?: AbortSignal): Promise<ApiResult<IEvaluation>> =>
  apiGet<IEvaluation>(`${EVALUATION_BASE}/${evaluationId}`, { signal });

export const createManagerworkEvaluation = (employeId: number, signal?: AbortSignal): Promise<ApiResult<IEvaluation>> =>
  apiPost<IEvaluation, Partial<IEvaluation>>(
    EVALUATION_BASE,
    {
      employe: { id: employeId },
      dateDebut: dayjs(),
      conforme: false,
    },
    { signal },
  );

export const updateManagerworkEvaluation = (evaluation: IEvaluation, signal?: AbortSignal): Promise<ApiResult<IEvaluation>> =>
  apiPut<IEvaluation, IEvaluation>(`${EVALUATION_BASE}/${evaluation.id}`, evaluation, { signal });
