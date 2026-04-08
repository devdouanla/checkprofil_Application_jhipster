export interface ManagerworkRuntimeConfig {
  routeBasePath: string;
  apiPrefix: string;
  endpoints: Record<string, string>;
}

const defaultManagerworkConfig: ManagerworkRuntimeConfig = {
  routeBasePath: '/managerwork',
  apiPrefix: '/api',
  endpoints: {
    managers: 'managers',
    evaluations: 'evaluations',
    employes: 'employes',
    postes: 'postes',
    competenceRequises: 'competence-requises',
    epreuves: 'epreuves',
    questions: 'questions',
    sessionTests: 'session-tests',
  },
};

declare global {
  interface Window {
    __MANAGERWORK_CONFIG__?: Partial<ManagerworkRuntimeConfig>;
  }
}

const trimSlashes = (value: string): string => value.replace(/^\/+|\/+$/g, '');

export const getManagerworkConfig = (): ManagerworkRuntimeConfig => {
  const runtime =
    typeof window !== 'undefined' && window.__MANAGERWORK_CONFIG__
      ? window.__MANAGERWORK_CONFIG__
      : ({} as Partial<ManagerworkRuntimeConfig>);

  return {
    routeBasePath: runtime.routeBasePath ?? defaultManagerworkConfig.routeBasePath,
    apiPrefix: runtime.apiPrefix ?? defaultManagerworkConfig.apiPrefix,
    endpoints: {
      ...defaultManagerworkConfig.endpoints,
      ...(runtime.endpoints ?? {}),
    },
  };
};

export const getManagerworkRouteBasePath = (): string => {
  const base = getManagerworkConfig().routeBasePath || defaultManagerworkConfig.routeBasePath;
  return base.startsWith('/') ? base : `/${base}`;
};

export const getManagerworkRoutePath = (): string => `${getManagerworkRouteBasePath().replace(/\/$/, '')}/*`;

export const buildManagerworkRoute = (suffix = ''): string => {
  const normalizedSuffix = suffix ? `/${trimSlashes(suffix)}` : '';
  return `${getManagerworkRouteBasePath().replace(/\/$/, '')}${normalizedSuffix}`;
};

export const getManagerworkApiEndpoint = (resourceKey: keyof ManagerworkRuntimeConfig['endpoints']): string => {
  const { apiPrefix, endpoints } = getManagerworkConfig();
  const resource = endpoints[resourceKey];
  const prefix = trimSlashes(apiPrefix || defaultManagerworkConfig.apiPrefix);
  return `/${prefix}/${trimSlashes(resource)}`;
};
