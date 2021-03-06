package databute.databutee.console;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import databute.databutee.Callback;
import databute.databutee.Databutee;
import databute.databutee.DatabuteeConfiguration;
import databute.databutee.DatabuteeConstants;
import databute.databutee.bucket.Bucket;
import databute.databutee.entry.EmptyEntryKeyException;
import databute.databutee.entry.Entry;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.apache.commons.lang3.StringUtils;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public final class Console {

    private static Console instance;

    private Databutee databutee;
    private final Scanner scanner;

    private Console() {
        this.scanner = new Scanner(System.in);
    }

    private void start() {
        while (true) {
            final String line = scanner.nextLine();
            final List<String> words = Lists.newArrayList(line.split(" "));
            if (words.size() == 0) {
                continue;
            }
            if (StringUtils.equalsAny(words.get(0), "quit", "exit")) {
                break;
            } else if (StringUtils.equals(words.get(0), "connect")) {
                if (databutee != null) {
                    System.out.println("already connected.");
                    continue;
                }

                final List<InetSocketAddress> addresses = words.subList(1, words.size())
                        .stream()
                        .map(address -> {
                            final String[] hostnameAndPort = address.split(":");
                            if (hostnameAndPort.length == 1) {
                                final String hostname = hostnameAndPort[0];
                                return new InetSocketAddress(hostname, DatabuteeConstants.DEFAULT_PORT);
                            } else {
                                final String hostname = hostnameAndPort[0];
                                final int port = Integer.parseInt(hostnameAndPort[1]);
                                return new InetSocketAddress(hostname, port);
                            }
                        })
                        .collect(Collectors.toList());

                try {
                    databutee = new Databutee(new DatabuteeConfiguration() {
                        @Override
                        public EventLoopGroup loopGroup() {
                            return new NioEventLoopGroup();
                        }

                        @Override
                        public List<InetSocketAddress> addresses() {
                            return addresses;
                        }
                    });
                    databutee.connect();
                    System.out.println("connected.");
                } catch (ConnectException e) {
                    System.err.println("Failed to connect to " + addresses);

                    databutee = null;
                }
            } else if (StringUtils.equals(words.get(0), "buckets")) {
                if (databutee == null) {
                    System.out.println("not connected yet");
                } else {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Buckets").append(System.lineSeparator());
                    for (Bucket bucket : databutee.bucketGroup()) {
                        sb.append("\t").append(bucket).append(System.lineSeparator());
                    }
                    sb.append(System.lineSeparator());
                    System.out.println(sb.toString());
                }
            } else if (StringUtils.equals(words.get(0), "get")) {
                if (databutee == null) {
                    System.out.println("not connected yet");
                } else {
                    final String key = words.get(1);
                    try {
                        databutee.get(key, new Callback() {
                            @Override
                            public void onSuccess(Entry entry) {
                                System.out.println("entry = " + entry);
                            }

                            @Override
                            public void onFailure(Exception e) {
                                System.err.println(e.toString());
                            }
                        });
                    } catch (EmptyEntryKeyException e) {
                        System.err.println("key must not be empty.");
                    }
                }
            } else if (StringUtils.equals(words.get(0), "set")) {
                if (databutee == null) {
                    System.out.println("not connected yet");
                } else {
                    final String type = words.get(1);

                    if (StringUtils.equals(type, "int")) {
                        final String key = words.get(2);
                        final Integer value = Integer.parseInt(words.get(3));
                        try {
                            databutee.setInteger(key, value, new Callback() {
                                @Override
                                public void onSuccess(Entry entry) {
                                    System.out.println("entry = " + entry);
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    System.err.println(e.toString());
                                }
                            });
                        } catch (EmptyEntryKeyException e) {
                            System.err.println("key must not be empty.");
                        }
                    } else if (StringUtils.equals(type, "long")) {
                        final String key = words.get(2);
                        final Long value = Long.parseLong(words.get(3));
                        try {
                            databutee.setLong(key, value, new Callback() {
                                @Override
                                public void onSuccess(Entry entry) {
                                    System.out.println("entry = " + entry);
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    System.err.println(e.toString());
                                }
                            });
                        } catch (EmptyEntryKeyException e) {
                            System.err.println("key must not be empty.");
                        }
                    } else if (StringUtils.equals(type, "string")) {
                        final String key = words.get(2);
                        final String value = words.get(3);
                        try {
                            databutee.setString(key, value, new Callback() {
                                @Override
                                public void onSuccess(Entry entry) {
                                    System.out.println("entry = " + entry);
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    System.err.println(e.toString());
                                }
                            });
                        } catch (EmptyEntryKeyException e) {
                            System.err.println("key must not be empty.");
                        }
                    } else if (StringUtils.equals(type, "list")) {
                        final String key = words.get(2);
                        final List<String> value = words.subList(3, words.size());
                        try {
                            databutee.setList(key, value, new Callback() {
                                @Override
                                public void onSuccess(Entry entry) {
                                    System.out.println(entry.toString());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    System.err.println(e.toString());
                                }
                            });
                        } catch (EmptyEntryKeyException e) {
                            System.err.println("key must not be empty.");
                        }
                    } else if (StringUtils.equals(type, "set")) {
                        final String key = words.get(2);
                        final List<String> items = words.subList(3, words.size());
                        final Set<String> value = Sets.newHashSet(items);
                        try {
                            databutee.setSet(key, value, new Callback() {
                                @Override
                                public void onSuccess(Entry entry) {
                                    System.out.println(entry.toString());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    System.err.println(e.toString());
                                }
                            });
                        } catch (EmptyEntryKeyException e) {
                            System.err.println("key must not be empty.");
                        }
                    } else if (StringUtils.equals(type, "dict")) {
                        final String key = words.get(2);
                        final List<String> items = words.subList(3, words.size());
                        final Iterator<String> itemIterator = items.iterator();

                        final Map<String, String> value = Maps.newHashMap();
                        while (itemIterator.hasNext()) {
                            final String itemKey = itemIterator.next();
                            final String item = itemIterator.next();

                            value.put(itemKey, item);
                        }
                        try {
                            databutee.setDictionary(key, value, new Callback() {
                                @Override
                                public void onSuccess(Entry entry) {
                                    System.out.println(entry.toString());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    System.err.println(e.toString());
                                }
                            });
                        } catch (EmptyEntryKeyException e) {
                            System.err.println("key must not be empty.");
                        }
                    }
                }
            } else if (StringUtils.equals(words.get(0), "update")) {
                if (databutee == null) {
                    System.out.println("not connected yet");
                } else {
                    final String type = words.get(1);

                    if (StringUtils.equals(type, "int")) {
                        final String key = words.get(2);
                        final Integer value = Integer.parseInt(words.get(3));
                        try {
                            databutee.updateInteger(key, value, new Callback() {
                                @Override
                                public void onSuccess(Entry entry) {
                                    System.out.println(entry.toString());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    System.err.println(e.toString());
                                }
                            });
                        } catch (EmptyEntryKeyException e) {
                            System.err.println("key must not be empty.");
                        }
                    } else if (StringUtils.equals(type, "long")) {
                        final String key = words.get(2);
                        final Long value = Long.parseLong(words.get(3));
                        try {
                            databutee.updateLong(key, value, new Callback() {
                                @Override
                                public void onSuccess(Entry entry) {
                                    System.out.println(entry.toString());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    System.err.println(e.toString());
                                }
                            });
                        } catch (EmptyEntryKeyException e) {
                            System.err.println("key must not be empty.");
                        }
                    } else if (StringUtils.equals(type, "string")) {
                        final String key = words.get(2);
                        final String value = words.get(3);
                        try {
                            databutee.updateString(key, value, new Callback() {
                                @Override
                                public void onSuccess(Entry entry) {
                                    System.out.println(entry.toString());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    System.err.println(e.toString());
                                }
                            });
                        } catch (EmptyEntryKeyException e) {
                            System.err.println("key must not be empty.");
                        }
                    } else if (StringUtils.equals(type, "list")) {
                        final String key = words.get(2);
                        final List<String> value = words.subList(3, words.size());
                        try {
                            databutee.updateList(key, value, new Callback() {
                                @Override
                                public void onSuccess(Entry entry) {
                                    System.out.println(entry.toString());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    System.err.println(e.toString());
                                }
                            });
                        } catch (EmptyEntryKeyException e) {
                            System.err.println("key must not be empty.");
                        }
                    } else if (StringUtils.equals(type, "set")) {
                        final String key = words.get(2);
                        final List<String> items = words.subList(3, words.size());
                        final Set<String> value = Sets.newHashSet(items);
                        try {
                            databutee.updateSet(key, value, new Callback() {
                                @Override
                                public void onSuccess(Entry entry) {
                                    System.out.println(entry.toString());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    System.err.println(e.toString());
                                }
                            });
                        } catch (EmptyEntryKeyException e) {
                            System.err.println("key must not be empty.");
                        }
                    } else if (StringUtils.equals(type, "dict")) {
                        final String key = words.get(2);
                        final List<String> items = words.subList(3, words.size());
                        final Iterator<String> itemIterator = items.iterator();

                        final Map<String, String> value = Maps.newHashMap();
                        while (itemIterator.hasNext()) {
                            final String itemKey = itemIterator.next();
                            itemIterator.remove();
                            final String item = itemIterator.next();
                            itemIterator.remove();

                            value.put(itemKey, item);
                        }
                        try {
                            databutee.updateDictionary(key, value, new Callback() {
                                @Override
                                public void onSuccess(Entry entry) {
                                    System.out.println(entry.toString());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    System.err.println(e.toString());
                                }
                            });
                        } catch (EmptyEntryKeyException e) {
                            System.err.println("key must not be empty.");
                        }
                    }
                }
            } else if (StringUtils.equals(words.get(0), "delete")) {
                if (databutee == null) {
                    System.out.println("not connected yet");
                } else {
                    final String key = words.get(1);
                    try {
                        databutee.delete(key, new Callback() {
                            @Override
                            public void onSuccess(Entry entry) {
                                System.out.println(entry.toString());
                            }

                            @Override
                            public void onFailure(Exception e) {
                                System.err.println(e.toString());
                            }
                        });
                    } catch (EmptyEntryKeyException e) {
                        System.err.println("key must not be empty.");
                    }
                }
            } else if (StringUtils.equals(words.get(0), "expire")) {
                if (databutee == null) {
                    System.out.println("not connected yet");
                } else {
                    final String key = words.get(1);
                    final long expireAfter = Long.parseLong(words.get(2));
                    final Instant expireAt = Instant.now().plusSeconds(expireAfter);
                    try {
                        databutee.expire(key, expireAt, new Callback() {
                            @Override
                            public void onSuccess(Entry entry) {
                                System.out.println(entry.toString());
                            }

                            @Override
                            public void onFailure(Exception e) {
                                System.err.println(e.toString());
                            }
                        });
                    } catch (EmptyEntryKeyException e) {
                        System.err.println("key must not be empty.");
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            instance = new Console();
            instance.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
