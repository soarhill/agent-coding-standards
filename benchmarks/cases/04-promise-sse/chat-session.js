let eventSource = null;

export async function sendMessage({
  fetchEventSource,
  url,
  payload,
  onChunk,
  setLoading,
  setError,
}) {
  setLoading(true);
  const controller = new AbortController();

  try {
    fetchEventSource(url, {
      method: "POST",
      body: JSON.stringify(payload),
      signal: controller.signal,
      onmessage(event) {
        onChunk(event.data);
      },
    });
  } catch (error) {
    setError(error.message);
  } finally {
    setLoading(false);
  }
}

export function closeSSE() {
  if (eventSource) {
    eventSource.close();
  }
}
