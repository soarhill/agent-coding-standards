import test from "node:test";
import assert from "node:assert/strict";
import { closeSSE, sendMessage } from "./chat-session.js";

test("sendMessage stays loading until the SSE promise completes", async () => {
  const states = [];
  let finish;

  const pending = new Promise((resolve) => {
    finish = resolve;
  });

  const run = sendMessage({
    fetchEventSource: async (_url, options) => {
      options.onmessage({ data: "hello" });
      await pending;
    },
    url: "/chat",
    payload: { text: "hi" },
    onChunk: () => {},
    setLoading: (value) => states.push(value),
    setError: () => {},
  });

  await Promise.resolve();
  assert.deepEqual(states, [true]);

  finish();
  await run;
  assert.deepEqual(states, [true, false]);
});

test("sendMessage reports async failure", async () => {
  const errors = [];

  await sendMessage({
    fetchEventSource: async () => {
      await Promise.resolve();
      throw new Error("network down");
    },
    url: "/chat",
    payload: {},
    onChunk: () => {},
    setLoading: () => {},
    setError: (value) => errors.push(value),
  });

  assert.deepEqual(errors, ["network down"]);
});

test("closeSSE aborts the active request", async () => {
  let aborted = false;
  let release;

  const waiting = new Promise((resolve) => {
    release = resolve;
  });

  const run = sendMessage({
    fetchEventSource: async (_url, options) => {
      options.signal.addEventListener("abort", () => {
        aborted = true;
        release();
      }, { once: true });
      await waiting;
    },
    url: "/chat",
    payload: {},
    onChunk: () => {},
    setLoading: () => {},
    setError: () => {},
  });

  await Promise.resolve();
  closeSSE();
  await run;

  assert.equal(aborted, true);
});
