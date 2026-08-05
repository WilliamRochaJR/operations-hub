const apiUrl = import.meta.env.VITE_API_URL ?? 'http://localhost:3000/api';

export async function http<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(`${apiUrl}${path}`, {
    ...init,
    headers: { 'Content-Type': 'application/json', ...init?.headers },
  });

  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(error.detail ?? 'Não foi possível concluir a operação.');
  }

  return response.json() as Promise<T>;
}
