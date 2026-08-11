"""Unit tests for the RTP video parser.

This file is not part of the runtime Qt application; it is a quick guardrail to
validate the packet parsing logic without needing the full drone stack.
"""

import importlib.util
import io
import os
import sys
import unittest

SPEC = importlib.util.spec_from_file_location(
    "decoder",
    os.path.join(os.path.dirname(__file__), "..", "bin", "decoder.py"),
)
mod = importlib.util.module_from_spec(SPEC)
SPEC.loader.exec_module(mod)


class DecoderTests(unittest.TestCase):
    def _capture_stdout(self, func, *args):
        buf = io.BytesIO()
        old_stdout = sys.stdout

        class DummyStdout:
            def __init__(self, stream):
                self.buffer = stream

        sys.stdout = DummyStdout(buf)
        try:
            func(*args)
        finally:
            sys.stdout = old_stdout
        return buf.getvalue()

    def test_packet_with_jpeg_payload_is_detected(self):
        jpeg = b"\xFF\xD8test\xFF\xD9"
        packet = jpeg

        data = self._capture_stdout(mod.process_packet, packet)
        self.assertTrue(data.startswith(b"\x00\x00\x00"))
        self.assertIn(jpeg, data)

    def test_h264_payload_is_recognized_as_h264(self):
        payload = b"\x00\x00\x00\x01\x67\x42\x00\x1e\x00\x00\x00\x01\x68\x00\x00"
        self.assertTrue(mod.looks_like_h264(payload))
        # A partial NAL sequence is not a complete access unit, so decoding may
        # legitimately return None until more H.264 packets are assembled.
        self.assertIsNone(mod.process_rtp_h264(payload))


if __name__ == "__main__":
    unittest.main()
