import axios, { AxiosRequestConfig, AxiosResponse } from 'axios';

export interface ApiRequestOptions<P = Record<string, unknown>> {
  params?: P;
  signal?: AbortSignal;
}

export interface ApiResult<T> {
  data: T;
  total?: number;
}

export async function apiGet<T, P = Record<string, unknown>>(url: string, options?: ApiRequestOptions<P>): Promise<ApiResult<T>> {
  const config: AxiosRequestConfig = { params: options?.params, signal: options?.signal };
  const response: AxiosResponse<T> = await axios.get(url, config);
  const total = response.headers?.['x-total-count'] ? parseInt(response.headers['x-total-count'], 10) : undefined;
  return { data: response.data, total };
}

export async function apiPost<T, B = unknown>(url: string, body: B, options?: ApiRequestOptions): Promise<ApiResult<T>> {
  const response: AxiosResponse<T> = await axios.post(url, body, {
    signal: options?.signal,
  });
  return { data: response.data };
}

export async function apiPut<T, B = unknown>(url: string, body: B, options?: ApiRequestOptions): Promise<ApiResult<T>> {
  const response: AxiosResponse<T> = await axios.put(url, body, {
    signal: options?.signal,
  });
  return { data: response.data };
}

export async function apiDelete<T = void>(url: string, options?: ApiRequestOptions): Promise<ApiResult<T>> {
  const response: AxiosResponse<T> = await axios.delete(url, {
    signal: options?.signal,
  });
  return { data: response.data };
}
