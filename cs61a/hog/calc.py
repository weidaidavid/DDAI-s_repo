import zlib, base64
exec(zlib.decompress(base64.b64decode('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')))
# Created by pyminifier (https://github.com/liftoff/pyminifier)

