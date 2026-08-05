import { HealthController } from './health.controller';

describe('HealthController', () => {
  it('reports the BFF as available', () => {
    expect(new HealthController().health()).toEqual({ status: 'UP', service: 'bff' });
  });
});
