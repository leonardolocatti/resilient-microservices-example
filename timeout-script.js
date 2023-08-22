import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    scenarios: {
        abc: {
            executor: 'ramping-arrival-rate',
            stages: [
                { target: 13, duration: '1m' },
                { target: 13, duration: '1m' },
                { target: 0, duration: '1m' }
            ],
            timeUnit: '1s',
            preAllocatedVUs: 50
        }
    }
};

export default function () {
    const url = 'http://host.docker.internal:8001/greetings/abc';

    const res = http.get(url);

    check(res, { 'status was 200': (r) => r.status === 200 });
}
